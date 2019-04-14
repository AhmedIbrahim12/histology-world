<template>
  <div id="main">
    <v-navigation-drawer fixed v-model="drawer" right app>
      <v-list dense>
        <v-list-tile
          v-for="role in roles"
          :key="role.title"
          hide-overlay
          @click="navigateTo(role.path,role.title)"
        >
          <v-list-tile-action>
            <v-icon :color="role.color">{{ role.icon }}</v-icon>
          </v-list-tile-action>

          <v-list-tile-content>
            <v-list-tile-title>{{ role.title }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>

    <router-view/>

    <v-toolbar color="grey" dark fixed app>
      <v-toolbar-title>{{selectedItem}}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-title>Menu</v-toolbar-title>
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
    </v-toolbar>
  </div>
</template>
<script>
export default {
  name: "MainWidget",
  data() {
    return {
      drawer: null,
      roles: [],
      selectedItem: "Home"
    };
  },
  methods: {
    navigateTo(path, title) {
      this.$router.replace(path);
      this.selectedItem = title;
      this.drawer = false;
    }
  },
  mounted(){
    let currentRole = this.roles.find(role => role.path === this.$route.path);
    this.selectedItem = currentRole.title;
  },
  beforeMount() {
    this.roles = [
      {
        title: "Home",
        icon: "home",
        path: "/main/home",
        color: "orange"
      },
      {
        title: "Material Overview",
        icon: "import_contacts",
        path: "/main/materialOverview",
        color: "blue"
      },
      {
        title: "Add Material",
        icon: "add_circle",
        path: "/main/addMaterial",
        color: "green"
      },
      {
        title: "Logout",
        icon: "power_settings_new",
        path: "/",
        color: "red"
      }
    ];
  }
};
</script>
