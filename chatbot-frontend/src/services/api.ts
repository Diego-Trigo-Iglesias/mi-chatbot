import axios from 'axios';

export const api = axios.create({
  baseURL: 'http://localhost:8080/api/chat'
});

export interface ChatMessage {
  id: number;
  userId: string;
  message: string;
  fromUser: boolean;
  timestamp: string;
}

export interface ChatMessageDTO {
  userId: string;
  message: string;
}

export function sendMessage(dto: ChatMessageDTO) {
  return api.post<ChatMessage>('/send', dto);
}

export function getHistory(userId: string) {
  return api.get<ChatMessage[]>(`/history/${userId}`);
}
