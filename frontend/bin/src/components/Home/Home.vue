<template>
  <div id="addMaterial" style="padding: 2% 3% 5% 5%;">
    <v-card>
      <v-layout justify-space-between pa-3>
        <v-flex xs6>
          <v-treeview
            :active.sync="active"
            :items="items"
            :load-children="fetchChildren"
            :open.sync="open"
            activatable
            active-class="primary--text"
            class="grey lighten-5"
            open-on-click
            transition
            item-text="title"
          >
            <template v-slot:prepend="{ item, active }">
              <v-icon :color="active ? 'primary' : ''">description</v-icon>
            </template>
          </v-treeview>
        </v-flex>

        <v-flex d-flex text-xs-center>
          <v-scroll-y-transition mode="out-in">
            <div
              v-if="!selected"
              class="title grey--text text--lighten-1 font-weight-light"
              style="align-self: center;"
            >Select a lecture</div>

            <v-card v-else :key="selected.id" class="pt-4 mx-auto" flat max-width="400">
              <v-card-text>
                <h3 class="headline mb-2">{{ selected.title }}</h3>
                <div class="black--text mb-2">{{ selected.description }}</div>
              </v-card-text>
              <v-divider></v-divider>

              <v-layout tag="v-card-text" text-xs-center wrap>
                <v-flex
                  v-if="selected.attachmentDownloadUrl"
                  tag="strong"
                  xs10
                >
                  <a
                    :href="selected.attachmentDownloadUrl"
                    target="_blank"
                  >Download {{ selected.title }}</a>
                </v-flex>
              </v-layout>
            </v-card>
          </v-scroll-y-transition>
        </v-flex>
      </v-layout>
    </v-card>
  </div>
</template>

<script>
import { AXIOS } from "../api-src";
export default {
  name: "Home",
  data: () => ({
    active: [],
    open: [],
    materials: []
  }),

  computed: {
    items() {
      return [
        {
          title: "Material",
          children: this.materials
        }
      ];
    },
    selected() {
      if (!this.active.length) return undefined;
      const id = this.active[0];
      return this.materials.find(material => material.id === id);
    }
  },

  methods: {
    async fetchChildren(item) {
      return AXIOS.get("/lecture/getAll")
        .then(res => res.data)
        .then(data => item.children.push(...data))
        .catch(err => console.warn(err));
    }
  }
};
</script>


