import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';
import { expect } from '@jest/globals';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;
  const mockTeachers: Teacher[] = [
    {
      id: 1,
      lastName: 'Doe',
      firstName: 'John',
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];
  const mockTeacher: Teacher = {
    id: 1,
    lastName: 'Doe',
    firstName: 'John',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'il n'y a pas de requêtes en suspens
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('all', () => {
    it('should return an Observable of Teacher array', () => {
      service.all().subscribe(teachers => {
        expect(teachers.length).toBe(1);
        expect(teachers).toEqual(mockTeachers);
      });

      const req = httpMock.expectOne('api/teacher');
      expect(req.request.method).toBe('GET');
      req.flush(mockTeachers);
    });
  });

  describe('detail', () => {
    it('should return an Observable of Teacher', () => {
      const teacherId = '1';

      service.detail(teacherId).subscribe(teacher => {
        expect(teacher).toEqual(mockTeacher);
      });

      const req = httpMock.expectOne(`api/teacher/${teacherId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockTeacher);
    });

    it('should handle different id types', () => {
      const teacherId = 'test-id';

      service.detail(teacherId).subscribe(teacher => {
        expect(teacher).toBeTruthy();
      });

      const req = httpMock.expectOne(`api/teacher/${teacherId}`);
      req.flush(mockTeacher);
    });
  });
});