/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.ibm.eventautomation.kafka.schemas;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class OnlineOrder extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -337292512224099877L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"OnlineOrder\",\"namespace\":\"com.ibm.eventautomation.kafka.schemas\",\"fields\":[{\"name\":\"id\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"},\"doc\":\"Unique ID for the online order\"},{\"name\":\"customer\",\"type\":{\"type\":\"record\",\"name\":\"Customer\",\"fields\":[{\"name\":\"id\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"},\"doc\":\"Unique id for the customer\"},{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Name of the customer\"},{\"name\":\"emails\",\"type\":{\"type\":\"array\",\"items\":\"string\"},\"doc\":\"Emails of the customer\"}]},\"doc\":\"Customer who made the online order\"},{\"name\":\"products\",\"type\":{\"type\":\"array\",\"items\":\"string\"},\"doc\":\"Descriptions of the ordered products\"},{\"name\":\"address\",\"type\":{\"type\":\"record\",\"name\":\"OnlineAddress\",\"fields\":[{\"name\":\"shippingaddress\",\"type\":{\"type\":\"record\",\"name\":\"ShippingAddress\",\"fields\":[{\"name\":\"number\",\"type\":[\"null\",\"int\"],\"doc\":\"House number for the shipping address\"},{\"name\":\"street\",\"type\":[\"null\",\"string\"],\"doc\":\"Street for the shipping address\"},{\"name\":\"city\",\"type\":\"string\",\"doc\":\"City for the shipping address\"},{\"name\":\"zipcode\",\"type\":\"string\",\"doc\":\"Zipcode for the shipping address\"},{\"name\":\"country\",\"type\":{\"type\":\"record\",\"name\":\"ShippingCountry\",\"fields\":[{\"name\":\"code\",\"type\":\"string\",\"doc\":\"Two-letter country code\"},{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Name of the country\"}]},\"doc\":\"Country for the shipping address\"},{\"name\":\"phones\",\"type\":[\"null\",{\"type\":\"array\",\"items\":\"string\"}],\"doc\":\"Phone numbers for the shipping address\"}]},\"doc\":\"Shipping address used for the online order\"},{\"name\":\"billingaddress\",\"type\":{\"type\":\"record\",\"name\":\"BillingAddress\",\"fields\":[{\"name\":\"number\",\"type\":[\"null\",\"int\"],\"doc\":\"House number for the billing address\"},{\"name\":\"street\",\"type\":[\"null\",\"string\"],\"doc\":\"Street for the billing address\"},{\"name\":\"city\",\"type\":\"string\",\"doc\":\"City for the billing address\"},{\"name\":\"zipcode\",\"type\":\"string\",\"doc\":\"Zipcode for the billing address\"},{\"name\":\"country\",\"type\":{\"type\":\"record\",\"name\":\"BillingCountry\",\"fields\":[{\"name\":\"code\",\"type\":\"string\",\"doc\":\"Two-letter country code\"},{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Name of the country\"}]},\"doc\":\"Country for the billing address\"},{\"name\":\"phones\",\"type\":[\"null\",{\"type\":\"array\",\"items\":\"string\"}],\"doc\":\"Phone numbers for the billing address\"}]},\"doc\":\"Billing address used for the online order\"}]},\"doc\":\"Address information used for the online order\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();
  static {
    MODEL$.addLogicalTypeConversion(new org.apache.avro.Conversions.UUIDConversion());
  }

  private static final BinaryMessageEncoder<OnlineOrder> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<OnlineOrder> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<OnlineOrder> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<OnlineOrder> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<OnlineOrder> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this OnlineOrder to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a OnlineOrder from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a OnlineOrder instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static OnlineOrder fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Unique ID for the online order */
  private java.util.UUID id;
  /** Customer who made the online order */
  private com.ibm.eventautomation.kafka.schemas.Customer customer;
  /** Descriptions of the ordered products */
  private java.util.List<java.lang.CharSequence> products;
  /** Address information used for the online order */
  private com.ibm.eventautomation.kafka.schemas.OnlineAddress address;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public OnlineOrder() {}

  /**
   * All-args constructor.
   * @param id Unique ID for the online order
   * @param customer Customer who made the online order
   * @param products Descriptions of the ordered products
   * @param address Address information used for the online order
   */
  public OnlineOrder(java.util.UUID id, com.ibm.eventautomation.kafka.schemas.Customer customer, java.util.List<java.lang.CharSequence> products, com.ibm.eventautomation.kafka.schemas.OnlineAddress address) {
    this.id = id;
    this.customer = customer;
    this.products = products;
    this.address = address;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return customer;
    case 2: return products;
    case 3: return address;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  private static final org.apache.avro.Conversion<?>[] conversions =
      new org.apache.avro.Conversion<?>[] {
      new org.apache.avro.Conversions.UUIDConversion(),
      null,
      null,
      null,
      null
  };

  @Override
  public org.apache.avro.Conversion<?> getConversion(int field) {
    return conversions[field];
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.util.UUID)value$; break;
    case 1: customer = (com.ibm.eventautomation.kafka.schemas.Customer)value$; break;
    case 2: products = (java.util.List<java.lang.CharSequence>)value$; break;
    case 3: address = (com.ibm.eventautomation.kafka.schemas.OnlineAddress)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return Unique ID for the online order
   */
  public java.util.UUID getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * Unique ID for the online order
   * @param value the value to set.
   */
  public void setId(java.util.UUID value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'customer' field.
   * @return Customer who made the online order
   */
  public com.ibm.eventautomation.kafka.schemas.Customer getCustomer() {
    return customer;
  }


  /**
   * Sets the value of the 'customer' field.
   * Customer who made the online order
   * @param value the value to set.
   */
  public void setCustomer(com.ibm.eventautomation.kafka.schemas.Customer value) {
    this.customer = value;
  }

  /**
   * Gets the value of the 'products' field.
   * @return Descriptions of the ordered products
   */
  public java.util.List<java.lang.CharSequence> getProducts() {
    return products;
  }


  /**
   * Sets the value of the 'products' field.
   * Descriptions of the ordered products
   * @param value the value to set.
   */
  public void setProducts(java.util.List<java.lang.CharSequence> value) {
    this.products = value;
  }

  /**
   * Gets the value of the 'address' field.
   * @return Address information used for the online order
   */
  public com.ibm.eventautomation.kafka.schemas.OnlineAddress getAddress() {
    return address;
  }


  /**
   * Sets the value of the 'address' field.
   * Address information used for the online order
   * @param value the value to set.
   */
  public void setAddress(com.ibm.eventautomation.kafka.schemas.OnlineAddress value) {
    this.address = value;
  }

  /**
   * Creates a new OnlineOrder RecordBuilder.
   * @return A new OnlineOrder RecordBuilder
   */
  public static com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder newBuilder() {
    return new com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder();
  }

  /**
   * Creates a new OnlineOrder RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new OnlineOrder RecordBuilder
   */
  public static com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder newBuilder(com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder other) {
    if (other == null) {
      return new com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder();
    } else {
      return new com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder(other);
    }
  }

  /**
   * Creates a new OnlineOrder RecordBuilder by copying an existing OnlineOrder instance.
   * @param other The existing instance to copy.
   * @return A new OnlineOrder RecordBuilder
   */
  public static com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder newBuilder(com.ibm.eventautomation.kafka.schemas.OnlineOrder other) {
    if (other == null) {
      return new com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder();
    } else {
      return new com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder(other);
    }
  }

  /**
   * RecordBuilder for OnlineOrder instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<OnlineOrder>
    implements org.apache.avro.data.RecordBuilder<OnlineOrder> {

    /** Unique ID for the online order */
    private java.util.UUID id;
    /** Customer who made the online order */
    private com.ibm.eventautomation.kafka.schemas.Customer customer;
    private com.ibm.eventautomation.kafka.schemas.Customer.Builder customerBuilder;
    /** Descriptions of the ordered products */
    private java.util.List<java.lang.CharSequence> products;
    /** Address information used for the online order */
    private com.ibm.eventautomation.kafka.schemas.OnlineAddress address;
    private com.ibm.eventautomation.kafka.schemas.OnlineAddress.Builder addressBuilder;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.customer)) {
        this.customer = data().deepCopy(fields()[1].schema(), other.customer);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (other.hasCustomerBuilder()) {
        this.customerBuilder = com.ibm.eventautomation.kafka.schemas.Customer.newBuilder(other.getCustomerBuilder());
      }
      if (isValidValue(fields()[2], other.products)) {
        this.products = data().deepCopy(fields()[2].schema(), other.products);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.address)) {
        this.address = data().deepCopy(fields()[3].schema(), other.address);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (other.hasAddressBuilder()) {
        this.addressBuilder = com.ibm.eventautomation.kafka.schemas.OnlineAddress.newBuilder(other.getAddressBuilder());
      }
    }

    /**
     * Creates a Builder by copying an existing OnlineOrder instance
     * @param other The existing instance to copy.
     */
    private Builder(com.ibm.eventautomation.kafka.schemas.OnlineOrder other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.customer)) {
        this.customer = data().deepCopy(fields()[1].schema(), other.customer);
        fieldSetFlags()[1] = true;
      }
      this.customerBuilder = null;
      if (isValidValue(fields()[2], other.products)) {
        this.products = data().deepCopy(fields()[2].schema(), other.products);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.address)) {
        this.address = data().deepCopy(fields()[3].schema(), other.address);
        fieldSetFlags()[3] = true;
      }
      this.addressBuilder = null;
    }

    /**
      * Gets the value of the 'id' field.
      * Unique ID for the online order
      * @return The value.
      */
    public java.util.UUID getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * Unique ID for the online order
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder setId(java.util.UUID value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * Unique ID for the online order
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * Unique ID for the online order
      * @return This builder.
      */
    public com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'customer' field.
      * Customer who made the online order
      * @return The value.
      */
    public com.ibm.eventautomation.kafka.schemas.Customer getCustomer() {
      return customer;
    }


    /**
      * Sets the value of the 'customer' field.
      * Customer who made the online order
      * @param value The value of 'customer'.
      * @return This builder.
      */
    public com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder setCustomer(com.ibm.eventautomation.kafka.schemas.Customer value) {
      validate(fields()[1], value);
      this.customerBuilder = null;
      this.customer = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'customer' field has been set.
      * Customer who made the online order
      * @return True if the 'customer' field has been set, false otherwise.
      */
    public boolean hasCustomer() {
      return fieldSetFlags()[1];
    }

    /**
     * Gets the Builder instance for the 'customer' field and creates one if it doesn't exist yet.
     * Customer who made the online order
     * @return This builder.
     */
    public com.ibm.eventautomation.kafka.schemas.Customer.Builder getCustomerBuilder() {
      if (customerBuilder == null) {
        if (hasCustomer()) {
          setCustomerBuilder(com.ibm.eventautomation.kafka.schemas.Customer.newBuilder(customer));
        } else {
          setCustomerBuilder(com.ibm.eventautomation.kafka.schemas.Customer.newBuilder());
        }
      }
      return customerBuilder;
    }

    /**
     * Sets the Builder instance for the 'customer' field
     * Customer who made the online order
     * @param value The builder instance that must be set.
     * @return This builder.
     */

    public com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder setCustomerBuilder(com.ibm.eventautomation.kafka.schemas.Customer.Builder value) {
      clearCustomer();
      customerBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'customer' field has an active Builder instance
     * Customer who made the online order
     * @return True if the 'customer' field has an active Builder instance
     */
    public boolean hasCustomerBuilder() {
      return customerBuilder != null;
    }

    /**
      * Clears the value of the 'customer' field.
      * Customer who made the online order
      * @return This builder.
      */
    public com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder clearCustomer() {
      customer = null;
      customerBuilder = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'products' field.
      * Descriptions of the ordered products
      * @return The value.
      */
    public java.util.List<java.lang.CharSequence> getProducts() {
      return products;
    }


    /**
      * Sets the value of the 'products' field.
      * Descriptions of the ordered products
      * @param value The value of 'products'.
      * @return This builder.
      */
    public com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder setProducts(java.util.List<java.lang.CharSequence> value) {
      validate(fields()[2], value);
      this.products = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'products' field has been set.
      * Descriptions of the ordered products
      * @return True if the 'products' field has been set, false otherwise.
      */
    public boolean hasProducts() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'products' field.
      * Descriptions of the ordered products
      * @return This builder.
      */
    public com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder clearProducts() {
      products = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'address' field.
      * Address information used for the online order
      * @return The value.
      */
    public com.ibm.eventautomation.kafka.schemas.OnlineAddress getAddress() {
      return address;
    }


    /**
      * Sets the value of the 'address' field.
      * Address information used for the online order
      * @param value The value of 'address'.
      * @return This builder.
      */
    public com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder setAddress(com.ibm.eventautomation.kafka.schemas.OnlineAddress value) {
      validate(fields()[3], value);
      this.addressBuilder = null;
      this.address = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'address' field has been set.
      * Address information used for the online order
      * @return True if the 'address' field has been set, false otherwise.
      */
    public boolean hasAddress() {
      return fieldSetFlags()[3];
    }

    /**
     * Gets the Builder instance for the 'address' field and creates one if it doesn't exist yet.
     * Address information used for the online order
     * @return This builder.
     */
    public com.ibm.eventautomation.kafka.schemas.OnlineAddress.Builder getAddressBuilder() {
      if (addressBuilder == null) {
        if (hasAddress()) {
          setAddressBuilder(com.ibm.eventautomation.kafka.schemas.OnlineAddress.newBuilder(address));
        } else {
          setAddressBuilder(com.ibm.eventautomation.kafka.schemas.OnlineAddress.newBuilder());
        }
      }
      return addressBuilder;
    }

    /**
     * Sets the Builder instance for the 'address' field
     * Address information used for the online order
     * @param value The builder instance that must be set.
     * @return This builder.
     */

    public com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder setAddressBuilder(com.ibm.eventautomation.kafka.schemas.OnlineAddress.Builder value) {
      clearAddress();
      addressBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'address' field has an active Builder instance
     * Address information used for the online order
     * @return True if the 'address' field has an active Builder instance
     */
    public boolean hasAddressBuilder() {
      return addressBuilder != null;
    }

    /**
      * Clears the value of the 'address' field.
      * Address information used for the online order
      * @return This builder.
      */
    public com.ibm.eventautomation.kafka.schemas.OnlineOrder.Builder clearAddress() {
      address = null;
      addressBuilder = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public OnlineOrder build() {
      try {
        OnlineOrder record = new OnlineOrder();
        record.id = fieldSetFlags()[0] ? this.id : (java.util.UUID) defaultValue(fields()[0]);
        if (customerBuilder != null) {
          try {
            record.customer = this.customerBuilder.build();
          } catch (org.apache.avro.AvroMissingFieldException e) {
            e.addParentField(record.getSchema().getField("customer"));
            throw e;
          }
        } else {
          record.customer = fieldSetFlags()[1] ? this.customer : (com.ibm.eventautomation.kafka.schemas.Customer) defaultValue(fields()[1]);
        }
        record.products = fieldSetFlags()[2] ? this.products : (java.util.List<java.lang.CharSequence>) defaultValue(fields()[2]);
        if (addressBuilder != null) {
          try {
            record.address = this.addressBuilder.build();
          } catch (org.apache.avro.AvroMissingFieldException e) {
            e.addParentField(record.getSchema().getField("address"));
            throw e;
          }
        } else {
          record.address = fieldSetFlags()[3] ? this.address : (com.ibm.eventautomation.kafka.schemas.OnlineAddress) defaultValue(fields()[3]);
        }
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<OnlineOrder>
    WRITER$ = (org.apache.avro.io.DatumWriter<OnlineOrder>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<OnlineOrder>
    READER$ = (org.apache.avro.io.DatumReader<OnlineOrder>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}










