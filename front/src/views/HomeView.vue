<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const posts = ref([]);

axios.get("/api/posts?page=1&size=5").then((response)=>{
  response.data.forEach((r:any)=>{
    posts.value.push(r)
  });
});

</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link :to="{ name: 'read', params: { postId: post.id } }">{{ post.title }}</router-link>
      </div>
      <div class="content">
        {{post.content}}
      </div>

      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2023-06-24</div>
      </div>
    </li>
  </ul>
</template>

<style scoped lang="scss">
ul{
  list-style: none;
  padding: 0;
}

li{
  margin-bottom: 2rem;
}

li .title a{
  font-size: 1.1rem;
  color: #383838;
  text-decoration: none;
}

li .title a:hover {
  text-decoration: underline;
}

li .content{
  font-size: 0.85rem;
  margin-top: 8px;
  color: #7e7e7e;
}

li:last-child{
  margin-bottom: 0;
}

.sub {
  margin-top: 8px;
  font-size: 0.78rem;
}

.sub .regDate {
  color: #6b6b6b;
}

.regDate{
  margin-left: 10px;
  color: #6b6b6b;
}
</style>