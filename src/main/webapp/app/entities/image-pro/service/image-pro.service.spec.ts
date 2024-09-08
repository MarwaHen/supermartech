import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IImagePro } from '../image-pro.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../image-pro.test-samples';

import { ImageProService } from './image-pro.service';

const requireRestSample: IImagePro = {
  ...sampleWithRequiredData,
};

describe('ImagePro Service', () => {
  let service: ImageProService;
  let httpMock: HttpTestingController;
  let expectedResult: IImagePro | IImagePro[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ImageProService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ImagePro', () => {
      const imagePro = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(imagePro).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImagePro', () => {
      const imagePro = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(imagePro).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImagePro', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImagePro', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImagePro', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addImageProToCollectionIfMissing', () => {
      it('should add a ImagePro to an empty array', () => {
        const imagePro: IImagePro = sampleWithRequiredData;
        expectedResult = service.addImageProToCollectionIfMissing([], imagePro);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imagePro);
      });

      it('should not add a ImagePro to an array that contains it', () => {
        const imagePro: IImagePro = sampleWithRequiredData;
        const imageProCollection: IImagePro[] = [
          {
            ...imagePro,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImageProToCollectionIfMissing(imageProCollection, imagePro);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImagePro to an array that doesn't contain it", () => {
        const imagePro: IImagePro = sampleWithRequiredData;
        const imageProCollection: IImagePro[] = [sampleWithPartialData];
        expectedResult = service.addImageProToCollectionIfMissing(imageProCollection, imagePro);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imagePro);
      });

      it('should add only unique ImagePro to an array', () => {
        const imageProArray: IImagePro[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const imageProCollection: IImagePro[] = [sampleWithRequiredData];
        expectedResult = service.addImageProToCollectionIfMissing(imageProCollection, ...imageProArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const imagePro: IImagePro = sampleWithRequiredData;
        const imagePro2: IImagePro = sampleWithPartialData;
        expectedResult = service.addImageProToCollectionIfMissing([], imagePro, imagePro2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imagePro);
        expect(expectedResult).toContain(imagePro2);
      });

      it('should accept null and undefined values', () => {
        const imagePro: IImagePro = sampleWithRequiredData;
        expectedResult = service.addImageProToCollectionIfMissing([], null, imagePro, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imagePro);
      });

      it('should return initial array if no ImagePro is added', () => {
        const imageProCollection: IImagePro[] = [sampleWithRequiredData];
        expectedResult = service.addImageProToCollectionIfMissing(imageProCollection, undefined, null);
        expect(expectedResult).toEqual(imageProCollection);
      });
    });

    describe('compareImagePro', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImagePro(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareImagePro(entity1, entity2);
        const compareResult2 = service.compareImagePro(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareImagePro(entity1, entity2);
        const compareResult2 = service.compareImagePro(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareImagePro(entity1, entity2);
        const compareResult2 = service.compareImagePro(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
