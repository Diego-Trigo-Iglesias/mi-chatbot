<script lang="ts">
import { defineComponent, ref, onMounted, nextTick } from 'vue';
import { sendMessage, getHistory, ChatMessage, ChatMessageDTO } from '../services/api';

export default defineComponent({
  setup() {
    const userId = 'admin';
    const messages = ref<ChatMessage[]>([]);
    const input = ref('');
    const sending = ref(false);
    const messagesContainer = ref<HTMLElement>();

    const loadHistory = async () => {
      const res = await getHistory(userId);
      messages.value = res.data;
      scrollToBottom();
    };

    const handleSend = async () => {
      if (!input.value.trim()) return;
      sending.value = true;
      const dto: ChatMessageDTO = { userId, message: input.value };
      // mostramos localmente
      messages.value.push({
        id: Date.now(),
        userId,
        message: input.value,
        fromUser: true,
        timestamp: new Date().toISOString(),
      });
      scrollToBottom();
      // enviamos al backend
      try {
        const res = await sendMessage(dto);
        messages.value.push(res.data);
      } catch (e) {
        messages.value.push({
          id: Date.now() + 1,
          userId,
          message: 'Ha ocurrido un error, inténtalo de nuevo.',
          fromUser: false,
          timestamp: new Date().toISOString(),
        });
      }
      input.value = '';
      sending.value = false;
      scrollToBottom();
    };

    const scrollToBottom = () => {
      nextTick(() => {
        const el = messagesContainer.value;
        if (el) el.scrollTop = el.scrollHeight;
      });
    };

    const formatTime = (iso: string) => {
      const d = new Date(iso);
      return d.getHours().toString().padStart(2, '0') + ':' + d.getMinutes().toString().padStart(2, '0');
    };

    onMounted(loadHistory);

    return { messages, input, sending, handleSend, messagesContainer, formatTime };
  },
});
</script>

<template>
  <div class="chat-window">
    <div class="header">
      <h2>ChatBot</h2>
    </div>
    <div class="messages" ref="messagesContainer">
      <div
        v-for="msg in messages"
        :key="msg.id"
        :class="['bubble', msg.fromUser ? 'bubble-out' : 'bubble-in']"
      >
        {{ msg.message }}
        <span class="timestamp">{{ formatTime(msg.timestamp) }}</span>
      </div>
    </div>
    <form class="input-area" @submit.prevent="handleSend">
      <input
        v-model="input"
        type="text"
        placeholder="Escribe un mensaje..."
        @keyup.enter="handleSend"
      />
      <button type="submit" :disabled="sending">
        <svg viewBox="0 0 24 24" class="send-icon">
          <path
            d="M2 21l21-9L2 3v7l15 2-15 2v7z"
            fill="currentColor"
          />
        </svg>
      </button>
    </form>
  </div>
</template>

<style scoped>
.chat-window {
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 600px;
  margin: auto;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  font-family: 'Helvetica', sans-serif;
}

.header {
  background: #075e54;
  color: white;
  padding: 12px;
  text-align: center;
}

.messages {
  flex: 1;
  padding: 12px;
  background: #ece5dd;
  overflow-y: auto;
}

.bubble {
  max-width: 75%;
  margin-bottom: 8px;
  padding: 8px 12px;
  position: relative;
  line-height: 1.4;
}

.bubble-in {
  background: white;
  border-radius: 0 12px 12px 12px;
  align-self: flex-start;
}

.bubble-out {
  background: #dcf8c6;
  border-radius: 12px 0 12px 12px;
  align-self: flex-end;
}

.timestamp {
  display: block;
  font-size: 0.65rem;
  color: #999;
  margin-top: 4px;
  text-align: right;
}

.input-area {
  display: flex;
  padding: 8px;
  background: #f0f0f0;
}

.input-area input {
  flex: 1;
  padding: 10px;
  border: none;
  border-radius: 20px;
  margin-right: 8px;
  outline: none;
}

.input-area button {
  width: 40px;
  height: 40px;
  border: none;
  background: #075e54;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
}

.input-area button:disabled {
  background: #999;
  cursor: not-allowed;
}

.send-icon {
  width: 20px;
  height: 20px;
}
</style>
