import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../services/session.service';
import { UnauthGuard } from './unauth.guard';

describe('UnauthGuard', () => {
  let guard: UnauthGuard;
  let sessionService: SessionService;
  let routerMock: jest.Mocked<Router>;

  beforeEach(() => {
    routerMock = {
      navigate: jest.fn().mockImplementation(() => Promise.resolve(true))
    } as unknown as jest.Mocked<Router>;

    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        UnauthGuard,
        { provide: Router, useValue: routerMock },
        {
          provide: SessionService,
          useValue: {
            isLogged: false
          }
        }
      ]
    });

    guard = TestBed.inject(UnauthGuard);
    sessionService = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should redirect to rentals when logged in', () => {
    (sessionService as any).isLogged = true;
    const result = guard.canActivate();
    
    expect(result).toBe(false);
    expect(routerMock.navigate).toHaveBeenCalledWith(['rentals']);
  });

  it('should allow access when not logged in', () => {
    (sessionService as any).isLogged = false;
    const result = guard.canActivate();
    
    expect(result).toBe(true);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });
});