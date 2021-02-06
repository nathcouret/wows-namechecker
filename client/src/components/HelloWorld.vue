<template>
  <h1>{{ msg }}</h1>
  <button @click="count++">count is: {{ count }}</button>

  <p>
    <a href="https://vitejs.dev/guide/features.html" target="_blank">Vite Docs</a> |
    <a href="https://v3.vuejs.org/" target="_blank">Vue 3 Docs</a>
  </p>

  <div>
    <form v-on:submit="doSearch">
      <input type="text" v-model="search" />
    </form>
    <div>{{result}}</div>
  </div>
</template>

<script lang="ts">
import { ref, defineComponent } from 'vue'
import {compose, map, prop, join} from 'ramda'
export default defineComponent({
  name: 'HelloWorld',
  props: {
    msg: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      result: ''
    }
  },
  setup: () => {
    const search = ref('')
    const count = ref(0)
    const useScriptSetup = ref(false);
    const useTsPlugin = ref(false);
    return { count, useScriptSetup, useTsPlugin, search }
  },
  methods: {
    doSearch(event: Event) {
      event.preventDefault();
      event.stopPropagation();
      fetch(`http://localhost:8080/players?name=${this.search}&exact=false`)
        .then(response => response.json())
        .then(json =>{
          const extractName = (player: {name: string}) => player.name
          compose(
          join(','),
           map(prop("nickname"), json)
          )
          this.result = JSON.stringify(json);
        })
    }
  }
})
</script>

<style scoped>
a {
  color: #42b983;
}

label {
  margin: 0 0.5em;
  font-weight: bold;
}

code {
  background-color: #eee;
  padding: 2px 4px;
  border-radius: 4px;
  color: #304455;
}
</style>
