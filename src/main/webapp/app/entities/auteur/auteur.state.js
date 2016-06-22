(function() {
    'use strict';

    angular
        .module('projetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('auteur', {
            parent: 'entity',
            url: '/auteur?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.auteur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auteur/auteurs.html',
                    controller: 'AuteurController',
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
                    $translatePartialLoader.addPart('auteur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('auteur-detail', {
            parent: 'entity',
            url: '/auteur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.auteur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/auteur/auteur-detail.html',
                    controller: 'AuteurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('auteur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Auteur', function($stateParams, Auteur) {
                    return Auteur.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('auteur.new', {
            parent: 'auteur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auteur/auteur-dialog.html',
                    controller: 'AuteurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                prenom: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('auteur', null, { reload: true });
                }, function() {
                    $state.go('auteur');
                });
            }]
        })
        .state('auteur.edit', {
            parent: 'auteur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auteur/auteur-dialog.html',
                    controller: 'AuteurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Auteur', function(Auteur) {
                            return Auteur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auteur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('auteur.delete', {
            parent: 'auteur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/auteur/auteur-delete-dialog.html',
                    controller: 'AuteurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Auteur', function(Auteur) {
                            return Auteur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('auteur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
