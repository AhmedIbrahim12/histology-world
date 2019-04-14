<template>
  <div id="addMaterial" style="padding: 2% 3% 5% 5%;">
    <v-flex xs12 sm8 md7>
      <v-alert
        v-model="visible"
        :type="alert.alertType"
        transition="scale-transition"
        dismissible
      >{{alert.alertMessage}}.</v-alert>

      <v-card class="elevation-12">
        <v-card-text>
          <v-form ref="form">
            <v-text-field
              v-model="form.title"
              prepend-icon="title"
              name="title"
              label="Title"
              type="text"
              v-validate="'required'"
              :error-messages="errors.collect('title')"
              data-vv-name="title"
              required
            ></v-text-field>

            <v-textarea
              v-model="form.description"
              prepend-icon="text_fields"
              name="description"
              label="Description"
              id="description"
              v-validate="'required'"
              :error-messages="errors.collect('description')"
              data-vv-name="description"
              required
            ></v-textarea>

            <v-text-field
              v-model="form.attachmentName"
              prepend-icon="attachment"
              name="attachment"
              label="Attachment"
              type="text"
              disabled
            ></v-text-field>

            <upload-btn icon color="grey" :fileChangedCallback="onFileChange">
              <template slot="icon">
                <v-icon color="white">cloud_upload</v-icon>
              </template>
            </upload-btn>
            
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="save()">Save</v-btn>
          <v-btn color="red" class="white--text" @click="reset()">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-flex>
  </div>
</template>
<script>
import { AXIOS } from "../api-src";
export default {
  name: "AddMaterial",
  data() {
    return {
      form: {
        title: "",
        description: "",
        attachment: null,
        attachmentName: "",
        fileDownloadUri: ""
      },
      alert: {
        alertType: "error",
        alertMessage: ""
      },
      visible: false
    };
  },
  methods: {
    onFileChange(file) {
      this.form.attachment = file;
      this.form.attachmentName = file.name;
    },

    save() {
      if (this.isFormValid()) {
        if (this.form.attachment != null) {
          this.uploadFile();
        } else {
          this.doSave();
        }
      }
    },

    uploadFile() {
      var params = new FormData();
      params.append("file", this.form.attachment);
      AXIOS.post("/uploadFile", params)
        .then(response => {
          this.form.fileDownloadUri = response.data.fileDownloadUri;
          this.doSave();
        })
        .catch(error => this.onFailure(error.message));
    },

    doSave() {
      var params = new URLSearchParams();
      params.append("title", this.form.title);
      params.append("description", this.form.description);
      params.append("attachmentUrl", this.form.fileDownloadUri);
      AXIOS.post("/lecture/save", params)
        .then(response => {
          this.alert.alertType = "success";
          this.alert.alertMessage = "Material saved successfully";
          this.visible = true;
        })
        .catch(error => this.onFailure(error.message));
    },

    onFailure(message) {
      console.log(message);
      this.alert.alertType = "error";
      this.alert.alertMessage = message;
      this.alert.visible = true;
    },

    reset() {
      this.alert.visible = false;
      this.$refs.form.reset();
      this.$validator.reset();
    },

    isFormValid() {
      this.$validator.validateAll();
      return this.fields.title.valid && this.fields.description.valid;
    }
  }
};
</script>