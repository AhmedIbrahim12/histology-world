<template>
  <v-dialog v-model="dialog" max-width="500px">
    <v-card>
      <v-card-title>
        <span class="headline">{{ formTitle }}</span>
      </v-card-title>

      <v-card-text>
        <v-form ref="form">
          <v-text-field
            v-model="editedItem.title"
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
            v-model="editedItem.description"
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
            v-model="editedItem.attachmentDownloadUrl"
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
        <v-btn color="blue darken-1" flat @click="close">Cancel</v-btn>
        <v-btn color="blue darken-1" flat @click="save">Save</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script>
import { AXIOS } from "../api-src";
export default {
  name: "EditMaterialDialog",
  props: ["dialog", "editedItem", "editedIndex"],
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "New Material" : "Edit Material";
    }
  },
  methods: {
    close() {
      this.$emit("dialogCloseEvent", false, -1);
      this.reset();
    },

    save() {
      if (this.isFormValid()) {
        this.$emit("dialogSaveEvent", false, this.editedIndex, this.editedItem);
      }
    },

    onFileChange(file) {
      if (file != null) {
        this.uploadFile(file);
      }
    },

    uploadFile(file) {
      var params = new FormData();
      params.append("file", file);
      AXIOS.post("/uploadFile", params)
        .then(resp => resp.data)
        .then(data => this.$emit("dialogFileChangeEvent", data.fileDownloadUri))
        .catch(error => this.onFailure(error.message));
    },

    reset() {
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

