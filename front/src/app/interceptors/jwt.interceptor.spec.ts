import { TestBed } from '@angular/core/testing';
import { HttpHandler, HttpRequest, HttpResponse } from '@angular/common/http';
import { SessionService } from '../services/session.service';
import { JwtInterceptor } from './jwt.interceptor';
import { expect } from '@jest/globals';
import { of } from 'rxjs';

describe('JwtInterceptor', () => {
  let interceptor: JwtInterceptor;
  let sessionService: SessionService;
  let mockHandler: jest.Mocked<HttpHandler>;

  beforeEach(() => {
    mockHandler = {
      handle: jest.fn().mockReturnValue(of(new HttpResponse()))
    } as unknown as jest.Mocked<HttpHandler>;

    TestBed.configureTestingModule({
      providers: [
        JwtInterceptor,
        {
          provide: SessionService,
          useValue: {
            isLogged: false,
            sessionInformation: {
              token: 'fake-token'
            }
          }
        }
      ]
    });

    interceptor = TestBed.inject(JwtInterceptor);
    sessionService = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should not add Authorization header when not logged in', () => {
    // Given
    (sessionService as any).isLogged = false;
    const mockRequest = new HttpRequest('GET', '/api/test');

    // When
    interceptor.intercept(mockRequest, mockHandler);

    // Then
    expect(mockHandler.handle).toHaveBeenCalledWith(mockRequest);
    expect(mockRequest.headers.has('Authorization')).toBeFalsy();
  });

  it('should add Authorization header when logged in', () => {
    // Given
    (sessionService as any).isLogged = true;
    const mockRequest = new HttpRequest('GET', '/api/test');

    // When
    interceptor.intercept(mockRequest, mockHandler);

    // Then
    const interceptedRequest = mockHandler.handle.mock.calls[0][0] as HttpRequest<any>;
    expect(interceptedRequest.headers.has('Authorization')).toBeTruthy();
    expect(interceptedRequest.headers.get('Authorization')).toBe('Bearer fake-token');
  });

  it('should pass through the request unchanged when not logged in', () => {
    // Given
    (sessionService as any).isLogged = false;
    const mockRequest = new HttpRequest('GET', '/api/test');

    // When
    interceptor.intercept(mockRequest, mockHandler);

    // Then
    expect(mockHandler.handle).toHaveBeenCalledWith(mockRequest);
  });

  it('should clone the request when adding Authorization header', () => {
    // Given
    (sessionService as any).isLogged = true;
    const mockRequest = new HttpRequest('GET', '/api/test');

    // When
    interceptor.intercept(mockRequest, mockHandler);

    // Then
    expect(mockHandler.handle).not.toHaveBeenCalledWith(mockRequest);
    const interceptedRequest = mockHandler.handle.mock.calls[0][0] as HttpRequest<any>;
    expect(interceptedRequest).not.toBe(mockRequest);
  });
});