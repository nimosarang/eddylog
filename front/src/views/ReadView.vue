<script setup lang="ts">

import {onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  },
});

const post = ref({
  id: 0,
  title: "",
  content: "",
})

const router = useRouter();

const moveToEdit = () => {
  router.push({name: "edit", params: {postId: props.postId}})
}

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`)
  .then((response) => {
    post.value = response.data
  });
});

</script>

<template>
  <el-row>
    <el-col>
      <h2 class="title">{{ post.title }}</h2>
      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2023-06-24 01:03:05</div>
      </div>
      <div class="sub">

      </div>
    </el-col>
  </el-row>

  <el-row>
    <el-col class="mt-3">
      <div class="content">{{ post.content }}</div>
    </el-col>
  </el-row>

  <el-row>
    <el-col class="mt-3">
      <div>
        <el-button type="warning" @click="moveToEdit">수정</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped>
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin: 0;
}

.sub {
  margin-top: 4px;
  font-size: 0.78rem;
}

.sub .regDate {
  color: #6b6b6b;
}

.regDate {
  margin-left: 10px;
  color: #6b6b6b;
}

.content {
  font-size: 0.85rem;
  margin-top: 8px;
  color: #7e7e7e;
  white-space: break-spaces;
  line-height: 1.5;
}
</style>