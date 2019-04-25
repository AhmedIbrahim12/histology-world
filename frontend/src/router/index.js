import Vue from 'vue'
import Router from 'vue-router'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'
import VueSession from 'vue-session'
import VeeValidate from 'vee-validate'
import UploadButton from 'vuetify-upload-button';

import Login from '../components/Login/Login.vue'
import MainWidget from '../components/MainView/MainWidget.vue'
import Footer from '../components/generic/Footer.vue'
import Home from '../components/Home/Home.vue'
import AddMaterial from '../components/AddMaterial/AddMaterial.vue'
import MaterialOverview from '../components/AddMaterial/MaterialOverview.vue'

Vue.component('widgeFooter', Footer);

Vue.use(VueSession)
Vue.use(VeeValidate)
Vue.use(Router)
Vue.use(Vuetify)
Vue.use(Vuetify, {
  iconfont: 'md'
})
Vue.use(UploadButton);
Vue.component('upload-btn', UploadButton);

export default new Router({
  base: '/histoworld/',
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/main',
      name: 'MainWidget',
      component: MainWidget,
      children: [
        {
          path: '',
          component: Home
        },
        {
          path: 'home',
          component: Home
        },
        {
          path: 'addMaterial',
          component: AddMaterial
        },
        {
          path: 'materialOverview',
          component: MaterialOverview
        }
      ]
    },

  ]
})
