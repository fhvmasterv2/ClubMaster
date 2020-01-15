import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClub } from 'app/shared/model/club.model';

type EntityResponseType = HttpResponse<IClub>;
type EntityArrayResponseType = HttpResponse<IClub[]>;

@Injectable({ providedIn: 'root' })
export class ClubService {
  public resourceUrl = SERVER_API_URL + 'api/clubs';

  constructor(protected http: HttpClient) {}

  create(club: IClub): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(club);
    return this.http
      .post<IClub>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(club: IClub): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(club);
    return this.http
      .put<IClub>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClub>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClub[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(club: IClub): IClub {
    const copy: IClub = Object.assign({}, club, {
      creationDate: club.creationDate && club.creationDate.isValid() ? club.creationDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? moment(res.body.creationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((club: IClub) => {
        club.creationDate = club.creationDate ? moment(club.creationDate) : undefined;
      });
    }
    return res;
  }
}
