import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITren, Tren } from '../tren.model';

import { TrenService } from './tren.service';

describe('Tren Service', () => {
  let service: TrenService;
  let httpMock: HttpTestingController;
  let elemDefault: ITren;
  let expectedResult: ITren | ITren[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrenService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      asientos: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Tren', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Tren()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tren', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          asientos: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tren', () => {
      const patchObject = Object.assign({}, new Tren());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tren', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          asientos: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Tren', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTrenToCollectionIfMissing', () => {
      it('should add a Tren to an empty array', () => {
        const tren: ITren = { id: 123 };
        expectedResult = service.addTrenToCollectionIfMissing([], tren);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tren);
      });

      it('should not add a Tren to an array that contains it', () => {
        const tren: ITren = { id: 123 };
        const trenCollection: ITren[] = [
          {
            ...tren,
          },
          { id: 456 },
        ];
        expectedResult = service.addTrenToCollectionIfMissing(trenCollection, tren);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tren to an array that doesn't contain it", () => {
        const tren: ITren = { id: 123 };
        const trenCollection: ITren[] = [{ id: 456 }];
        expectedResult = service.addTrenToCollectionIfMissing(trenCollection, tren);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tren);
      });

      it('should add only unique Tren to an array', () => {
        const trenArray: ITren[] = [{ id: 123 }, { id: 456 }, { id: 18954 }];
        const trenCollection: ITren[] = [{ id: 123 }];
        expectedResult = service.addTrenToCollectionIfMissing(trenCollection, ...trenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tren: ITren = { id: 123 };
        const tren2: ITren = { id: 456 };
        expectedResult = service.addTrenToCollectionIfMissing([], tren, tren2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tren);
        expect(expectedResult).toContain(tren2);
      });

      it('should accept null and undefined values', () => {
        const tren: ITren = { id: 123 };
        expectedResult = service.addTrenToCollectionIfMissing([], null, tren, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tren);
      });

      it('should return initial array if no Tren is added', () => {
        const trenCollection: ITren[] = [{ id: 123 }];
        expectedResult = service.addTrenToCollectionIfMissing(trenCollection, undefined, null);
        expect(expectedResult).toEqual(trenCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
