<template>
  <div style="padding: 2% 2% 2% 2%;">
    <v-alert
      v-model="visible"
      :type="alert.alertType"
      transition="scale-transition"
      dismissible
    >{{alert.alertMessage}}.</v-alert>

    <v-toolbar flat color="white">
      <v-toolbar-title>Add, update or delete material</v-toolbar-title>
      <v-divider class="mx-2" inset vertical></v-divider>

      <v-spacer></v-spacer>

      <v-btn color="primary" dark class="mb-2" @click="dialogCloseHandler(true,-1)">
        New Material
        <v-icon right dark>add_box</v-icon>
      </v-btn>

      <EditMaterialDialog
        :dialog="this.dialog"
        :editedItem="this.editedItem"
        :editedIndex="this.editedIndex"
        @dialogFileChangeEvent="dialogFileChangeHandler"
        @dialogCloseEvent="dialogCloseHandler"
        @dialogSaveEvent="dialogSaveHandler"
      ></EditMaterialDialog>
    </v-toolbar>

    <v-flex xs13>
      <v-data-table :headers="headers" :items="materials" class="elevation-1">
        <template v-slot:items="props">
          <td class="text-xs-left">{{ props.item.title }}</td>
          <td class="text-xs-left">{{ props.item.description }}</td>
          <td class="text-xs-left">{{ props.item.attachmentDownloadUrl }}</td>
          <td class="justify-center layout px-0">
            <v-icon small class="mr-2" @click="editItem(props.item)">edit</v-icon>
            <v-icon small @click="deleteItem(props.item)">delete</v-icon>
          </td>
        </template>
        <template v-slot:no-data>
          <p>No material found</p>
        </template>
      </v-data-table>
    </v-flex>
  </div>
</template>

<script>
import EditMaterialDialog from "./EditMaterialDialog.vue";
import { AXIOS } from "../api-src";

export default {
  name: "MaterialOverview",
  components: {
    EditMaterialDialog
  },
  data: () => ({
    dialog: false,
    headers: [
      {
        text: "Title",
        align: "left",
        sortable: true,
        value: "title"
      },
      {
        text: "Description",
        value: "description",
        sortable: false
      },
      {
        text: "Attachment Link",
        value: "attachmentDownloadUrls[0]",
        sortable: false
      },
      {
        text: "Actions",
        value: "name",
        sortable: false
      }
    ],
    materials: [],
    editedIndex: -1,
    editedItem: {
      id: Number,
      name: String,
      description: String,
      attachmentDownloadUrls: String
    },
    defaultItem: {
      id: null,
      name: "",
      description: "",
      attachmentDownloadUrls: ""
    },
    alert: {
      alertType: "error",
      alertMessage: ""
    },
    visible: false
  }),

  beforeMount() {
    AXIOS.get("/lecture/getAll")
      .then(res => res.data)
      .then(data => this.materials.push(...data))
      .catch(err => console.warn(err));
  },

  methods: {
    editItem(item) {
      this.editedIndex = this.materials.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      const index = this.materials.indexOf(item);
      let isDeleted =
        confirm("Are you sure you want to delete this item?") &&
        this.materials.splice(index, 1);
      if (isDeleted) {
        AXIOS.post("/lecture/delete", item)
          .then(resp => resp.data)
          .then(data => {
            this.alert.alertType = "success";
            this.alert.alertMessage = "Material deleted successfully";
            this.visible = true;
          });
      }
    },

    dialogCloseHandler(dialog, editedIndex) {
      this.dialog = dialog;
      this.editedItem = Object.assign({}, this.defaultItem);
      this.editedIndex = editedIndex;
    },

    dialogSaveHandler(dialog, editedIndex, editedItem) {
      if (editedIndex > -1) {
        Object.assign(this.materials[editedIndex], editedItem);
        this.updateMaterial(dialog, editedIndex);
      } else {
        this.materials.push(editedItem);
        this.doSave(dialog, editedIndex);
      }
    },

    updateMaterial(dialog, editedIndex) {
      AXIOS.post("/lecture/update", this.editedItem)
        .then(response => {
          this.alert.alertType = "success";
          this.alert.alertMessage = "Material updated successfully";
          this.visible = true;
          this.dialogCloseHandler(dialog, editedIndex);
        })
        .catch(error => this.onFailure(error.message));
    },

    doSave(dialog, editedIndex) {
      var params = new URLSearchParams();
      params.append("title", this.editedItem.title);
      params.append("description", this.editedItem.description);
      params.append("attachmentUrl", this.editedItem.fileDownloadUri);
      AXIOS.post("/lecture/save", params)
        .then(response => {
          this.alert.alertType = "success";
          this.alert.alertMessage = "Material saved successfully";
          this.visible = true;
          this.dialogCloseHandler(dialog, editedIndex);
        })
        .catch(error => this.onFailure(error.message));
    },

    onFailure(message) {
      console.log(message);
      this.alert.alertType = "error";
      this.alert.alertMessage = message;
      this.alert.visible = true;
    },

    dialogFileChangeHandler(file) {
      this.editedItem.attachmentDownloadUrl = file;
    }
  }
};
</script>