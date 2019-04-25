<template>
  <v-app id="inspire">
    <v-content>
      <v-container fluid>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md4>
            <v-alert
              v-model="alert"
              type="error"
              transition="scale-transition"
              dismissible
            >Incorret username or password.</v-alert>

            <v-card class="elevation-12">
              <v-toolbar dark color="primary">
                <v-toolbar-title>Login</v-toolbar-title>
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <v-text-field
                    v-model="loginData.username"
                    prepend-icon="person"
                    name="username"
                    label="Login"
                    type="text"
                    v-validate="'required'"
                    :error-messages="errors.collect('username')"
                    data-vv-name="username"
                    required
                  ></v-text-field>

                  <v-text-field
                    v-model="loginData.password"
                    prepend-icon="lock"
                    name="password"
                    label="Password"
                    id="password"
                    type="password"
                    v-validate="'required'"
                    :error-messages="errors.collect('password')"
                    data-vv-name="password"
                    required
                  ></v-text-field>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" @click="callLoginService()">Login</v-btn>
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
import { AXIOS } from "../api-src";

export default {
  name: "Login",
  data() {
    return {
      loginData: {
        username: "",
        password: ""
      },
      alert: false
    };
  },
  methods: {
    callLoginService() {
      console.log(process.env.API_ROOT);
      if (this.isFormValid()) {
        var params = new URLSearchParams();
        params.append("username", this.loginData.username);
        params.append("password", this.loginData.password);
        AXIOS.post(`/login`, params)
          .then(response => {
            if (response.data) {
              console.log(response.data);
              this.$session.set("basicAuthHeader",response.data);
              AXIOS.defaults.headers.common['Authorization'] = response.data;
              this.$router.replace("/main");
            } else {
              this.alert = true;
            }
          })
          .catch(e => {
            console.log(e);
          });
      }
    },

    isFormValid() {
      this.$validator.validateAll();
      return this.fields.username.valid && this.fields.password.valid;
    }
  }
};
</script>