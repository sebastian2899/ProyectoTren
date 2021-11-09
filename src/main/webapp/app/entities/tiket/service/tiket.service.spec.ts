import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITiket, Tiket } from '../tiket.model';

import { TiketService } from './tiket.service';

describe('Tiket Service', () => {
  let service: TiketService;
  let httpMock: HttpTestingController;
  let elemDefault: ITiket;
  let expectedResult: ITiket | ITiket[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TiketService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fecha: currentDate,
      trenId: 0,
      clienteId: 0,
      puesto: 0,
      estado: 'AAAAAAA',
      jordana: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Tiket', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.create(new Tiket()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tiket', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fecha: currentDate.format(DATE_TIME_FORMAT),
          trenId: 1,
          clienteId: 1,
          puesto: 1,
          estado: 'BBBBBB',
          jordana: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tiket', () => {
      const patchObject = Object.assign(
        {
          clienteId: 1,
          puesto: 1,
          estado: 'BBBBBB',
        },
        new Tiket()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tiket', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fecha: currentDate.format(DATE_TIME_FORMAT),
          trenId: 1,
          clienteId: 1,
          puesto: 1,
          estado: 'BBBBBB',
          jordana: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Tiket', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTiketToCollectionIfMissing', () => {
      it('should add a Tiket to an empty array', () => {
        const tiket: ITiket = { id: 123 };
        expectedResult = service.addTiketToCollectionIfMissing([], tiket);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tiket);
      });

      it('should not add a Tiket to an array that contains it', () => {
        const tiket: ITiket = { id: 123 };
        const tiketCollection: ITiket[] = [
          {
            ...tiket,
          },
          { id: 456 },
        ];
        expectedResult = service.addTiketToCollectionIfMissing(tiketCollection, tiket);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tiket to an array that doesn't contain it", () => {
        const tiket: ITiket = { id: 123 };
        const tiketCollection: ITiket[] = [{ id: 456 }];
        expectedResult = service.addTiketToCollectionIfMissing(tiketCollection, tiket);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tiket);
      });

      it('should add only unique Tiket to an array', () => {
        const tiketArray: ITiket[] = [{ id: 123 }, { id: 456 }, { id: 60676 }];
        const tiketCollection: ITiket[] = [{ id: 123 }];
        expectedResult = service.addTiketToCollectionIfMissing(tiketCollection, ...tiketArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tiket: ITiket = { id: 123 };
        const tiket2: ITiket = { id: 456 };
        expectedResult = service.addTiketToCollectionIfMissing([], tiket, tiket2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tiket);
        expect(expectedResult).toContain(tiket2);
      });

      it('should accept null and undefined values', () => {
        const tiket: ITiket = { id: 123 };
        expectedResult = service.addTiketToCollectionIfMissing([], null, tiket, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tiket);
      });

      it('should return initial array if no Tiket is added', () => {
        const tiketCollection: ITiket[] = [{ id: 123 }];
        expectedResult = service.addTiketToCollectionIfMissing(tiketCollection, undefined, null);
        expect(expectedResult).toEqual(tiketCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
