import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', () => {
  let service: AuthService;
  let httpClientMock: jest.Mocked<HttpClient>;

  const mockSessionInfo: SessionInformation = {
    token: 'fake-token',
    type: 'user',
    id: 1,
    username: 'test@test.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  };

  beforeEach(() => {
    // Création du mock pour HttpClient
    httpClientMock = {
      post: jest.fn()
    } as unknown as jest.Mocked<HttpClient>;

    TestBed.configureTestingModule({
      providers: [
        AuthService,
        { provide: HttpClient, useValue: httpClientMock }
      ]
    });

    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('register', () => {
    it('should call register endpoint with correct data', () => {
      const registerRequest: RegisterRequest = {
        email: 'test@test.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password123'
      };

      httpClientMock.post.mockReturnValue(of(null));

      service.register(registerRequest).subscribe();

      expect(httpClientMock.post).toHaveBeenCalledWith(
        'api/auth/register',
        registerRequest
      );
    });
  });

  describe('login', () => {
    it('should call login endpoint with correct data', () => {
      const loginRequest: LoginRequest = {
        email: 'test@test.com',
        password: 'password123'
      };

      httpClientMock.post.mockReturnValue(of(mockSessionInfo));

      service.login(loginRequest).subscribe(response => {
        expect(response).toEqual(mockSessionInfo);
      });

      expect(httpClientMock.post).toHaveBeenCalledWith(
        'api/auth/login',
        loginRequest
      );
    });
  });
});