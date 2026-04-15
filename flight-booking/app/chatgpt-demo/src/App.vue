<template>
  <div class="app-shell">
    <main class="app-main">
      <section class="workspace-panel booking-area">
        <div class="booking-table-shell">
          <el-table class="booking-table" :data="tableData" stripe table-layout="fixed" empty-text="暂无预订记录">
            <el-table-column prop="bookingNumber" label="预订号" min-width="92" show-overflow-tooltip />
            <el-table-column prop="name" label="乘机人" min-width="92" show-overflow-tooltip />
            <el-table-column label="出发日期" min-width="104" show-overflow-tooltip>
              <template #default="scope">
                {{ scope.row.begindate || scope.row.date || '暂无信息' }}
              </template>
            </el-table-column>
            <el-table-column prop="from" label="出发地" min-width="84" show-overflow-tooltip />
            <el-table-column prop="to" label="目的地" min-width="84" show-overflow-tooltip />
            <el-table-column prop="bookingStatus" label="状态" min-width="84">
              <template #default="scope">
                <el-tag
                  class="status-tag"
                  :type="getStatusTagType(scope.row.bookingStatus)"
                  effect="plain"
                >
                  {{ formatBookingStatus(scope.row.bookingStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="舱位" min-width="92" show-overflow-tooltip>
              <template #default="scope">
                {{ formatBookingClass(scope.row.bookingClass) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="128" align="center">
              <template #default>
                <div class="booking-actions booking-table__actions">
                  <el-button size="small" plain>更改预订</el-button>
                  <el-button size="small" type="danger" plain>退订</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="booking-card-list">
          <article
            v-for="(item, index) in tableData"
            :key="item.bookingNumber || index"
            class="booking-card"
          >
            <div class="booking-card__header">
              <div class="booking-card__header-main">
                <p class="booking-card__label">预订号</p>
                <h3 class="booking-card__title">{{ item.bookingNumber || '未生成' }}</h3>
                <p class="booking-card__meta">{{ item.name || '未填写乘机人' }}</p>
              </div>

              <el-tag
                class="status-tag"
                :type="getStatusTagType(item.bookingStatus)"
                effect="plain"
              >
                {{ formatBookingStatus(item.bookingStatus) }}
              </el-tag>
            </div>

            <div class="booking-card__grid">
              <div class="booking-card__cell">
                <p class="booking-card__label">出发日期</p>
                <p class="booking-card__subline">{{ item.begindate || item.date || '暂无信息' }}</p>
              </div>
              <div class="booking-card__cell">
                <p class="booking-card__label">舱位</p>
                <p class="booking-card__subline">{{ formatBookingClass(item.bookingClass) }}</p>
              </div>
              <div class="booking-card__cell">
                <p class="booking-card__label">出发地</p>
                <p class="booking-card__subline">{{ item.from || '暂无信息' }}</p>
              </div>
              <div class="booking-card__cell">
                <p class="booking-card__label">目的地</p>
                <p class="booking-card__subline">{{ item.to || '暂无信息' }}</p>
              </div>
            </div>

            <div class="booking-actions booking-card__actions">
              <el-button size="small" plain>更改预订</el-button>
              <el-button size="small" type="danger" plain>退订</el-button>
            </div>
          </article>

          <div v-if="!tableData.length" class="empty-booking-card">
            <p class="empty-booking-card__title">暂无预订记录</p>
          </div>
        </div>
      </section>

      <section class="workspace-panel workspace-panel--chat">
        <div class="chat-panel">
          <div class="chat-timeline">
            <el-timeline class="chat-thread">
              <el-timeline-item
                v-for="(activity, index) in activities"
                :key="index"
                :icon="activity.icon"
                :type="activity.type"
                :color="activity.color"
                :size="activity.size"
                :hollow="activity.hollow"
                class="chat-message-item"
                :class="activity.content.startsWith('你:') ? 'chat-message-item--user' : 'chat-message-item--assistant'"
              >
                <div class="chat-message">
                  <div class="chat-message__meta">{{ activity.timestamp }}</div>
                  <div
                    class="timeline-entry"
                    :class="activity.content.startsWith('你:') ? 'timeline-entry--user' : 'timeline-entry--assistant'"
                  >
                    {{ activity.content.startsWith('你:') ? activity.content.slice(2) : activity.content }}
                  </div>
                </div>
              </el-timeline-item>
            </el-timeline>
            <div ref="chatBottomRef" aria-hidden="true" style="height: 1px;" />
          </div>

          <div class="chat-composer">
            <div class="chat-composer__input-wrap">
              <el-input
                v-model="msg"
                class="chat-input"
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 6 }"
                placeholder="请输入您的出行需求或预订问题"
                @keydown.enter.prevent="sendMsg()"
              />

              <el-button
                class="send-button"
                type="primary"
                :icon="Top"
                aria-label="发送消息"
                title="发送消息"
                @click="sendMsg()"
              />
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>
<script lang="ts">
import { MoreFilled, Top } from '@element-plus/icons-vue'
import { nextTick, onMounted, ref } from 'vue'
import axios from 'axios'//引入axios

export default {
  setup() {
    const activities = ref([
      {
        content: '欢迎来到乐航航空智能助手，请告诉我您的出行需求。',
        timestamp: new Date().toLocaleDateString() + " " + new Date().toLocaleTimeString(),
        color: '#0bbd87',
      },
    ]);
    const msg = ref('');
    const tableData = ref([]);
    const chatBottomRef = ref<HTMLElement | null>(null);
    let count = 2;
    let eventSource: EventSource | null;

    const scrollChatToBottom = async () => {
      await nextTick();

      requestAnimationFrame(() => {
        chatBottomRef.value?.scrollIntoView({ block: 'end' });
      });
    };

    const formatBookingStatus = (status) => {
      if (status === 'CONFIRMED') {
        return '已确认';
      }

      if (status === 'CANCELLED') {
        return '已取消';
      }

      if (status === 'PENDING') {
        return '待处理';
      }

      return status || '暂无信息';
    };

    const getStatusTagType = (status) => {
      if (status === 'CONFIRMED') {
        return 'success';
      }

      if (status === 'CANCELLED') {
        return 'danger';
      }

      return 'info';
    };

    const formatBookingClass = (bookingClass) => {
      if (bookingClass === 'ECONOMY') {
        return '经济舱';
      }

      if (bookingClass === 'PREMIUM_ECONOMY') {
        return '高端经济舱';
      }

      if (bookingClass === 'BUSINESS') {
        return '商务舱';
      }

      if (bookingClass === 'FIRST') {
        return '头等舱';
      }

      return bookingClass || '暂无信息';
    };

    const sendMsg = () => {
      if (eventSource) {
        eventSource.close();
      }

      activities.value.push(
          {
            content: `你:${msg.value}`,
            timestamp: new Date().toLocaleDateString() + " " + new Date().toLocaleTimeString(),
            size: 'large',
            type: 'primary',
            icon: MoreFilled,
          },
      );

      activities.value.push(
          {
            content: '正在生成回复...',
            timestamp: new Date().toLocaleDateString() + " " + new Date().toLocaleTimeString(),
            color: '#0bbd87',
          },
      );
      void scrollChatToBottom();

      // sse: 服务端推送 Server-Sent Events
      eventSource = new EventSource(`http://localhost:8080/ai/generateStreamAsString?message=${msg.value}`);
      msg.value='';
      eventSource.onmessage = (event) => {
        activities.value[count].content += event.data;
        void scrollChatToBottom();
      };
      eventSource.onopen = (event) => {
        activities.value[count].content = '';
        void scrollChatToBottom();
      };
      /*eventSource.onerror = function (e) {
        count = count + 2;
        eventSource.close();
        getBookings();  // 每次对话完后刷新列表
      };*/
      eventSource.addEventListener('done', () => {
        count = count + 2;
        eventSource?.close();
        getBookings(); // 明确在“流完成”后刷新
      });
      eventSource.onerror = (e) => {
        console.error('chat stream error', e);
        eventSource?.close();
      };
    };

    const getBookings = () => {
      axios.get('http://localhost:8080/booking/list')
          .then((response) => {
            tableData.value = response.data;
          })
          .catch((error) => {
            console.error(error);
          });
    };

    // Use onMounted to call getBookings when the component is mounted
    onMounted(() => {
      getBookings();
    });

    return {
      activities,
      msg,
      tableData,
      chatBottomRef,
      formatBookingStatus,
      getStatusTagType,
      formatBookingClass,
      sendMsg,
      Top,
    };
  },
};
</script>
