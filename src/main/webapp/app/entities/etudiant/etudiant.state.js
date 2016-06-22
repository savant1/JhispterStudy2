(function() {
    'use strict';

    angular
        .module('projetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('etudiant', {
            parent: 'entity',
            url: '/etudiant?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.etudiant.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/etudiant/etudiants.html',
                    controller: 'EtudiantController',
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
                    $translatePartialLoader.addPart('etudiant');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('etudiant-detail', {
            parent: 'entity',
            url: '/etudiant/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.etudiant.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/etudiant/etudiant-detail.html',
                    controller: 'EtudiantDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('etudiant');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Etudiant', function($stateParams, Etudiant) {
                    return Etudiant.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('etudiant.new', {
            parent: 'etudiant',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etudiant/etudiant-dialog.html',
                    controller: 'EtudiantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                filiere: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('etudiant', null, { reload: true });
                }, function() {
                    $state.go('etudiant');
                });
            }]
        })
        .state('etudiant.edit', {
            parent: 'etudiant',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etudiant/etudiant-dialog.html',
                    controller: 'EtudiantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Etudiant', function(Etudiant) {
                            return Etudiant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('etudiant', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('etudiant.delete', {
            parent: 'etudiant',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etudiant/etudiant-delete-dialog.html',
                    controller: 'EtudiantDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Etudiant', function(Etudiant) {
                            return Etudiant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('etudiant', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
