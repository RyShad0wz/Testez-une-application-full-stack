import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';
import { Observable } from 'rxjs';

describe('SessionService', () => {
  let service: SessionService;
  const mockUser: SessionInformation = {
    token: 'mock-token',
    type: 'Bearer',
    id: 1,
    username: 'testuser',
    firstName: 'Test',
    lastName: 'User',
    admin: false
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  describe('$isLogged', () => {
    it('should return an Observable', () => {
      expect(service.$isLogged()).toBeInstanceOf(Observable);
    });

    it('should emit false initially', (done) => {
      service.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBe(false);
        done();
      });
    });
  });

  describe('logIn', () => {
    it('should set session information and isLogged to true', () => {
      service.logIn(mockUser);
      
      expect(service.isLogged).toBe(true);
      expect(service.sessionInformation).toEqual(mockUser);
    });

    it('should emit true through isLoggedSubject', (done) => {
      service.$isLogged().subscribe(isLogged => {
        if (isLogged) { // Skip initial false value
          expect(isLogged).toBe(true);
          done();
        }
      });
      
      service.logIn(mockUser);
    });
  });

  describe('logOut', () => {
    beforeEach(() => {
      service.logIn(mockUser); // First log in to test log out
    });

    it('should clear session information and set isLogged to false', () => {
      service.logOut();
      
      expect(service.isLogged).toBe(false);
      expect(service.sessionInformation).toBeUndefined();
    });

    it('should emit false through isLoggedSubject', (done) => {
      let emissions = 0;
      
      service.$isLogged().subscribe(isLogged => {
        emissions++;
        // First emission is true (from logIn), second is false (from logOut)
        if (emissions === 2) {
          expect(isLogged).toBe(false);
          done();
        }
      });
      
      service.logOut();
    });
  });

  describe('next', () => {
    it('should emit current isLogged state', (done) => {
      service['isLogged'] = true; // Force state for test
      
      service.$isLogged().subscribe(isLogged => {
        if (isLogged) { // Skip initial false value
          expect(isLogged).toBe(true);
          done();
        }
      });
      
      service['next'](); // Call private method for test
    });
  });
});