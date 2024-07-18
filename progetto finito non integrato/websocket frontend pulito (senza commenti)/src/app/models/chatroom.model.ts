import { User } from './user.model'; 

export interface ChatRoom {
  chatRoomId: string;
  senderId: User;
  receiverId: User;
}