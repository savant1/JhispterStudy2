(function() {
    'use strict';

    angular
        .module('projetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ouvrage', {
            parent: 'entity',
            url: '/ouvrage?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.ouvrage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ouvrage/ouvrages.html',
                    controller: 'OuvrageController',
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
                    $translatePartialLoader.addPart('ouvrage');
                    $translatePartialLoader.addPart('typeouvrage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ouvrage-detail', {
            parent: 'entity',
            url: '/ouvrage/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.ouvrage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ouvrage/ouvrage-detail.html',
                    controller: 'OuvrageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ouvrage');
                    $translatePartialLoader.addPart('typeouvrage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Ouvrage', function($stateParams, Ouvrage) {
                    return Ouvrage.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('ouvrage.new', {
            parent: 'ouvrage',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ouvrage/ouvrage-dialog.html',
                    controller: 'OuvrageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                filiere: null,
                                categorie: null,
                                discipline: null,
                                titre: null,
                                genre: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ouvrage', null, { reload: true });
                }, function() {
                    $state.go('ouvrage');
                });
            }]
        })
        .state('ouvrage.edit', {
            parent: 'ouvrage',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ouvrage/ouvrage-dialog.html',
                    controller: 'OuvrageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ouvrage', function(Ouvrage) {
                            return Ouvrage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ouvrage', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ouvrage.delete', {
            parent: 'ouvrage',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ouvrage/ouvrage-delete-dialog.html',
                    controller: 'OuvrageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ouvrage', function(Ouvrage) {
                            return Ouvrage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ouvrage', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
