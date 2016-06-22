(function() {
    'use strict';

    angular
        .module('projetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('professeur', {
            parent: 'entity',
            url: '/professeur?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.professeur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/professeur/professeurs.html',
                    controller: 'ProfesseurController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('professeur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('professeur-detail', {
            parent: 'entity',
            url: '/professeur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.professeur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/professeur/professeur-detail.html',
                    controller: 'ProfesseurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('professeur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Professeur', function($stateParams, Professeur) {
                    return Professeur.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('professeur.new', {
            parent: 'professeur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/professeur/professeur-dialog.html',
                    controller: 'ProfesseurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                departement: null,
                                profession: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('professeur', null, { reload: true });
                }, function() {
                    $state.go('professeur');
                });
            }]
        })
        .state('professeur.edit', {
            parent: 'professeur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/professeur/professeur-dialog.html',
                    controller: 'ProfesseurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Professeur', function(Professeur) {
                            return Professeur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('professeur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('professeur.delete', {
            parent: 'professeur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/professeur/professeur-delete-dialog.html',
                    controller: 'ProfesseurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Professeur', function(Professeur) {
                            return Professeur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('professeur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
