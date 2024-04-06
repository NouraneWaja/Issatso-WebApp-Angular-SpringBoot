// liste.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { GroupeService } from 'app/entities/groupe/service/groupe.service';

@Component({
  selector: 'jhi-liste',
  templateUrl: './liste.component.html',
  styleUrls: ['./liste.component.scss']
})
export class ListeComponent implements OnInit {
  etudiants: IEtudiant[] = [];

  constructor(
    protected groupeService: GroupeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      const id = params['id'];
      this.groupeService.getStudentsByGroup(id).subscribe(
        (response: IEtudiant[]) => {
          this.etudiants = response;
        },
        error => {
          console.error('Error fetching students:', error);
        }
      );
    });
  }
}

