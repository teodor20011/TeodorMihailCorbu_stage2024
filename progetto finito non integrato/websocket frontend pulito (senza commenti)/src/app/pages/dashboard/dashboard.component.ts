import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Observable, of, interval, Subscription } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { ChatService } from '../../service/chat.service';
import { Login } from '../login/login.component';
import { Message } from '../../models/message.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChatRoom } from '../../models/chatroom.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  users$: Observable<Login[]> | undefined;
  messages: Message[] = [];
  messageText: string = '';
  private refreshSubscription: Subscription | undefined;
  private messagesSubscription: Subscription | undefined;
  private chatPollingSubscription: Subscription | undefined;
  loggedNickname = sessionStorage.getItem('nickname');
  loggedUser: Login | undefined;
  selectedUser: Login | undefined;

  constructor(
    private http: HttpClient,
    private chatService: ChatService,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (this.loggedNickname) {
      this.http.get<Login>(`http://localhost:8080/user/${this.loggedNickname}`).pipe(
        catchError((error) => {
          console.error('Error fetching user:', error);
          return of(null);
        })
      ).subscribe((res: Login | null) => {
        if (res) {
          this.loggedUser = res;
          console.log('User fetched successfully:', this.loggedUser);
          debugger
          this.chatService.connectWebSocket(() => {
            debugger
            this.setupMessageSubscription();
            debugger
          });
        } else {
          console.error('Failed to fetch user');
        }
      });
    }
    
    this.refreshUsersList();
    this.refreshSubscription = interval(5000).pipe(
      switchMap(() => this.fetchLoggedInUsers())
    ).subscribe((users: Login[]) => {
      this.users$ = of(users);
    });

    this.messagesSubscription = this.chatService.messages$.pipe(
      catchError((error) => {
        console.error('Error receiving messages:', error);
        return of([]);
      })
    ).subscribe((messages: Message[]) => {
      debugger
      this.messages = messages;
      debugger
      this.cdRef.detectChanges();
      debugger
    });
  }

  setupMessageSubscription(): void {
    debugger
    if (this.loggedUser && this.selectedUser) {
      debugger
      this.chatService.getMessages(this.loggedUser, this.selectedUser);
    }
  }

  ngOnDestroy(): void {
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
    }
    if (this.messagesSubscription) {
      this.messagesSubscription.unsubscribe();
    }
  }

  fetchLoggedInUsers(): Observable<Login[]> {
    return this.http.get<Login[]>('http://localhost:8080/loggedInUsers').pipe(
      catchError((error) => {
        console.error('Error fetching logged in users:', error);
        return of([]);
      })
    );
  }

  refreshUsersList(): void {
    this.users$ = this.fetchLoggedInUsers();
  }

  startChat(receiver: Login): void {
    if (this.loggedUser) {
      this.selectedUser = receiver;
      const chatRoomId = this.generateChatRoomId(this.loggedUser.nickname, receiver.nickname);
      this.chatService.connectToChatRoom(chatRoomId, (message: Message) => {
        this.messages.push(message);
        debugger
        this.cdRef.detectChanges();
        debugger
      });
      debugger
      this.chatService.getMessages(this.loggedUser, receiver);
      this.startPollingMessages();
      debugger
      
      this.http.get<any[]>(`http://localhost:8080/between2usersMessages/${this.loggedUser.nickname}/${receiver.nickname}`).pipe(
        catchError((error) => {
          console.error('Error fetching messages:', error);
          return of([]);
        })
      ).subscribe(
        (messages: any[]) => {
          this.messages = messages;
          console.log('Messages received:', this.messages);
        });
      debugger
    }
  }

  onSubmit(): void {
    if (this.loggedUser && this.selectedUser) {
      const chatRoomId = this.generateChatRoomId(this.loggedUser.nickname, this.selectedUser.nickname);
      const chatRoomObj: ChatRoom = {
        chatRoomId: chatRoomId,
        senderId: this.loggedUser,
        receiverId: this.selectedUser
      };
      if (this.messageText.trim() === '') {
        return;
      }
      this.chatService.sendMessage(chatRoomObj, this.messageText, this.loggedUser, this.selectedUser);
     // this.messageText = '';
      debugger
      this.cdRef.detectChanges();

      this.http.get<any[]>(`http://localhost:8080/between2usersMessages/${this.loggedUser.nickname}/${this.selectedUser.nickname}`).pipe(
        catchError((error) => {
          console.error('Error fetching messages:', error);
          return of([]);
        })
      ).subscribe(
        (messages: any[]) => {
          this.messages = messages;
          console.log('Messages received:', this.messages);
        });
      debugger
    }
  }

startPollingMessages(): void {
  // Cancella eventuali polling esistenti
  if (this.chatPollingSubscription) {
    this.chatPollingSubscription.unsubscribe();
  }

  // Avvia un nuovo polling
  this.chatPollingSubscription = interval(300).pipe(
    switchMap(() => this.http.get<any[]>(`http://localhost:8080/between2usersMessages/${this.loggedUser!.nickname}/${this.selectedUser!.nickname}`).pipe(
      catchError((error) => {
        console.error('Error fetching messages:', error);
        return of([]);
      })
    ))
  ).subscribe(
    (messages: any[]) => {
      this.messages = messages;
      console.log('Messages received:', this.messages);
      this.cdRef.detectChanges();
    });
}

  private generateChatRoomId(nickname1: string, nickname2: string): string {
    return `${nickname1}_${nickname2}`;
  }
}
