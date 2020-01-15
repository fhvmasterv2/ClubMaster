import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { ClubService } from 'app/entities/club/club.service';
import { IClub, Club } from 'app/shared/model/club.model';
import { ClubType } from 'app/shared/model/enumerations/club-type.model';

describe('Service Tests', () => {
  describe('Club Service', () => {
    let injector: TestBed;
    let service: ClubService;
    let httpMock: HttpTestingController;
    let elemDefault: IClub;
    let expectedResult: IClub | IClub[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ClubService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Club(0, 'AAAAAAA', currentDate, ClubType.SPORT);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            creationDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Club', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            creationDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            creationDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Club())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Club', () => {
        const returnedFromService = Object.assign(
          {
            clubName: 'BBBBBB',
            creationDate: currentDate.format(DATE_FORMAT),
            type: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Club', () => {
        const returnedFromService = Object.assign(
          {
            clubName: 'BBBBBB',
            creationDate: currentDate.format(DATE_FORMAT),
            type: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            creationDate: currentDate
          },
          returnedFromService
        );
        service
          .query()
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Club', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
