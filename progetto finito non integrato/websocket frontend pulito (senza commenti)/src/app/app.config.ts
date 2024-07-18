import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

import { routes } from './app.routes'; 
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { customInterceptor } from './service/custom.interceptor';
import { ChatService } from './service/chat.service';  // Importa il servizio


export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideHttpClient(withInterceptors([customInterceptor]))]
};
