import { User } from './user.model'; 
import { ChatRoom } from './chatroom.model';


export interface Message {
  text: string;
  createdAt: Date;
  editable: boolean;
  chatIdentifier: ChatRoom;
  sender: User;
  receiver: User;
}
