import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';
import { expect } from '@jest/globals';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  
  const mockUser: User = {
    id: 1,
    email: 'test@example.com',
    lastName: 'Doe',
    firstName: 'John',
    admin: false,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });
    
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'il n'y a pas de requêtes HTTP en suspens
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getById', () => {
    it('should return a User observable for valid id', () => {
      const userId = '1';
      
      service.getById(userId).subscribe(user => {
        expect(user).toEqual(mockUser);
        expect(user.id).toBe(1);
      });

      const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockUser);
    });

    it('should handle different id formats', () => {
      const userId = 'test-id';
      
      service.getById(userId).subscribe(user => {
        expect(user).toBeTruthy();
      });

      const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
      req.flush(mockUser);
    });
  });

  describe('delete', () => {
    it('should send DELETE request for valid id', () => {
      const userId = '1';
      
      service.delete(userId).subscribe(response => {
        expect(response).toBeTruthy();
      });

      const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
    });

    it('should handle different id formats', () => {
      const userId = 'test-id';
      
      service.delete(userId).subscribe(response => {
        expect(response).toBeTruthy();
      });

      const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
      req.flush({});
    });
  });
});