import { Component, OnInit } from '@angular/core';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  imports: [CommonModule],
})
export class HeaderComponent implements OnInit {
  categories: ICategory[] = [];
  subCategories: ISubCategory[] = [];
  activeCategoryId: number | null = null;

  constructor(
    private categoryService: CategoryService,
    private subCategoryService: SubCategoryService,
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadSubCategories();
  }

  loadCategories(): void {
    this.categoryService.query().subscribe(response => {
      this.categories = response.body ?? [];
    });
  }

  loadSubCategories(): void {
    this.subCategoryService.query().subscribe(response => {
      this.subCategories = response.body ?? [];
      this.organizeSubCategories();
    });
  }

  organizeSubCategories(): void {
    this.categories.forEach(category => {
      category.subCategories = this.subCategories.filter(subCategory => subCategory.cat_id === category.id);
    });
  }

  showSubCategories(categoryId: number): void {
    this.activeCategoryId = categoryId;
  }

  hideSubCategories(): void {
    this.activeCategoryId = null;
  }
}
