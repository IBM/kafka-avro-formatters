/**
 * Copyright 2025 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.eventautomation.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpsURLConnectionTrustUpdater {

    private final Logger log = LoggerFactory.getLogger(HttpsURLConnectionTrustUpdater.class);

    private boolean skipHostnameValidation = true;

    public HttpsURLConnectionTrustUpdater(boolean skipHostnameValidation) {
        this.skipHostnameValidation = skipHostnameValidation;
    }


    /**
     * Updates the HttpsURLConnection instance so that it can be used for
     *  connections to servers using certificates signed by the provided
     *  CA pem file (in addition to existing well-known truststores
     *  registered with the JVM).
     */
    public void trustCustomCertAuthority(HttpsURLConnection conn, File pemFile) throws GeneralSecurityException, IOException {
        log.debug("Updating the HttpsURLConnection to trust the CA at {}", pemFile.getAbsolutePath());

        Certificate certAuthority = readCertificate(pemFile.toPath());
        KeyStore customStore = createCustomStore(certAuthority);
        TrustManager[] customStoreAndDefault = createCustomTrustManager(customStore);
        updateConnection(conn, customStoreAndDefault);
    }

    /**
     * Updates the HttpsURLConnection instance so that it can be used for
     *  connections to any https server without verifying the SSL/TLS
     *  certificate.
     */
    public void trustAnything(HttpsURLConnection conn) throws GeneralSecurityException {
        log.debug("Updating the HttpsURLConnection to trust any host");

        TrustManager[] anything = createTrustAnythingManager();
        updateConnection(conn, anything);
    }



    private void updateConnection(HttpsURLConnection conn, TrustManager[] trustMgrs) throws KeyManagementException, NoSuchAlgorithmException {
        SSLSocketFactory customSslFactory = createSslContextFactory(trustMgrs);
        conn.setSSLSocketFactory(customSslFactory);

        if (skipHostnameValidation) {
            conn.setHostnameVerifier(TrustAnyHostnameVerifier.INSTANCE);
        }
    }


    private Certificate readCertificate(Path pemFilePath) throws CertificateException, IOException {
        try (InputStream is = new BufferedInputStream(Files.newInputStream(pemFilePath))) {
            return (Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
        }
    }


    private KeyStore createCustomStore(Certificate customCertAuthority) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
        store.load(null, null);
        store.setCertificateEntry("ibm-eem-ca", customCertAuthority);
        return store;
    }


    private TrustManager[] createCustomTrustManager(KeyStore customStore) throws NoSuchAlgorithmException, KeyStoreException {
        final X509TrustManager defaultTrustManager = createTrustManager(null);
        final X509TrustManager customTrustManager = createTrustManager(customStore);

        return new TrustManager[] {
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    try {
                        defaultTrustManager.checkClientTrusted(chain, authType);
                    }
                    catch (CertificateException ce) {
                        customTrustManager.checkClientTrusted(chain, authType);
                    }
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    try {
                        defaultTrustManager.checkServerTrusted(chain, authType);
                    }
                    catch (CertificateException e) {
                        customTrustManager.checkServerTrusted(chain, authType);
                    }
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] defaultIssuers = defaultTrustManager.getAcceptedIssuers();
                    X509Certificate[] customIssuers = customTrustManager.getAcceptedIssuers();
                    X509Certificate[] allIssuers = Arrays.copyOf(defaultIssuers, defaultIssuers.length + customIssuers.length);
                    System.arraycopy(customIssuers, 0, allIssuers, defaultIssuers.length, customIssuers.length);
                    return allIssuers;
                }
            }
        };
    }


    private X509TrustManager createTrustManager(KeyStore source) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(source);

        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                return (X509TrustManager) tm;
            }
        }
        throw new IllegalStateException("No X509TrustManager found");
    }


    private SSLSocketFactory createSslContextFactory(TrustManager[] customTrustManagers) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, customTrustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
    }


    private TrustManager[] createTrustAnythingManager() {
        return new TrustManager[] {
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
        };
    }


    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public static final TrustAnyHostnameVerifier INSTANCE = new TrustAnyHostnameVerifier();

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
