import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { Message } from '../models/message.model';
import { User } from '../models/user.model';
import { ChatRoom } from '../models/chatroom.model';


@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private baseUrl = 'http://localhost:8080';
  private stompClient: Stomp.Client | null = null;
  private messageSubject: Subject<Message[]> = new Subject<Message[]>();
  public messages$: Observable<Message[]> = this.messageSubject.asObservable();

  constructor(private http: HttpClient) {}

  connectWebSocket(onConnect: () => void): void {
    const socket = new SockJS(`${this.baseUrl}/ws`);
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect('', '', () => {
      console.log('WebSocket connected successfully!');
      onConnect();
    });
  }

  connectToChatRoom(chatRoomId: string, onMessageReceived: (message: Message) => void): void {
    if (this.stompClient) {
      this.stompClient.subscribe(`/chatroom/${chatRoomId}`, (message: any) => {
        onMessageReceived(JSON.parse(message.body));
      });
    } else {
      console.error('WebSocket is not connected.');
    }
  }

  sendMessage(chatRoomId: ChatRoom, messageText: string, loggedInUser: User, receiver: User): void {
    const message: Message = {
      chatIdentifier: chatRoomId,
      createdAt: new Date(),
      text: messageText,
      editable: true,
      sender: loggedInUser,
      receiver: receiver
    };
    debugger
    this.stompClient?.send(`/app/message/${chatRoomId.chatRoomId}`, {}, JSON.stringify(message));
    debugger
  }

  getMessages(sender: User, receiver: User): void {
    if (this.stompClient && this.stompClient.connected) {
      const message = { sender, receiver };
      console.log('Messaggio inviato:', message);
      this.stompClient.send(`/app/between2usersMessages`, {}, JSON.stringify(message));
      debugger
      if (this.stompClient && this.stompClient.connected) {
        debugger
      this.stompClient.subscribe('/user/queue/messages', (messager: Stomp.Message) => { 
        debugger
        console.log('Messaggio ricevuto');
        debugger
        console.log('Messaggio ricevuto:', messager);
        debugger
        const messages: Message[] = JSON.parse(messager.body);
        debugger
        this.messageSubject.next(messages);
        debugger
      });
    } else {
      console.error('WebSocket is not connected.');
    }
    debugger
  }
    debugger
  }

  disconnectWebSocket(): void {
    if (this.stompClient) {
      this.stompClient.disconnect(() => {
        console.log('WebSocket disconnected successfully!');
      });
    }
  }
}
