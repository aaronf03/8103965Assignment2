package com.example.assignment2.data.repository;

import com.example.assignment2.data.remote.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DashboardRepository_Factory implements Factory<DashboardRepository> {
  private final Provider<ApiService> apiServiceProvider;

  public DashboardRepository_Factory(Provider<ApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public DashboardRepository get() {
    return newInstance(apiServiceProvider.get());
  }

  public static DashboardRepository_Factory create(Provider<ApiService> apiServiceProvider) {
    return new DashboardRepository_Factory(apiServiceProvider);
  }

  public static DashboardRepository newInstance(ApiService apiService) {
    return new DashboardRepository(apiService);
  }
}
