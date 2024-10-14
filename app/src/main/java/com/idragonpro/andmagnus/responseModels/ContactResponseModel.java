package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactResponseModel {

    @SerializedName("contact")
    @Expose
    private Contact contact;
    @SerializedName("status")
    @Expose
    private String status;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class Contact {

        @SerializedName("id")
        @Expose
        private long id;
        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("Contact")
        @Expose
        private String contact;
        @SerializedName("Customer_Support")
        @Expose
        private String customerSupport;
        @SerializedName("email_flag")
        @Expose
        private String emailFlag;
        @SerializedName("Contact_flag")
        @Expose
        private String contactFlag;
        @SerializedName("Customer_Support_flag")
        @Expose
        private String customerSupportFlag;
        @SerializedName("StatusId")
        @Expose
        private Object statusId;
        @SerializedName("StatusName")
        @Expose
        private Object statusName;
        @SerializedName("CreatedById")
        @Expose
        private Object createdById;
        @SerializedName("ModifiedById")
        @Expose
        private Object modifiedById;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getCustomerSupport() {
            return customerSupport;
        }

        public void setCustomerSupport(String customerSupport) {
            this.customerSupport = customerSupport;
        }

        public String getEmailFlag() {
            return emailFlag;
        }

        public void setEmailFlag(String emailFlag) {
            this.emailFlag = emailFlag;
        }

        public String getContactFlag() {
            return contactFlag;
        }

        public void setContactFlag(String contactFlag) {
            this.contactFlag = contactFlag;
        }

        public String getCustomerSupportFlag() {
            return customerSupportFlag;
        }

        public void setCustomerSupportFlag(String customerSupportFlag) {
            this.customerSupportFlag = customerSupportFlag;
        }

        public Object getStatusId() {
            return statusId;
        }

        public void setStatusId(Object statusId) {
            this.statusId = statusId;
        }

        public Object getStatusName() {
            return statusName;
        }

        public void setStatusName(Object statusName) {
            this.statusName = statusName;
        }

        public Object getCreatedById() {
            return createdById;
        }

        public void setCreatedById(Object createdById) {
            this.createdById = createdById;
        }

        public Object getModifiedById() {
            return modifiedById;
        }

        public void setModifiedById(Object modifiedById) {
            this.modifiedById = modifiedById;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }

    }
}
