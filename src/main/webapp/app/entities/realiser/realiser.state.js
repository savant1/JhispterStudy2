(function() {
    'use strict';

    angular
        .module('projetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('realiser', {
            parent: 'entity',
            url: '/realiser?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.realiser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/realiser/realisers.html',
                    controller: 'RealiserController',
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
                    $translatePartialLoader.addPart('realiser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('realiser-detail', {
            parent: 'entity',
            url: '/realiser/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.realiser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/realiser/realiser-detail.html',
                    controller: 'RealiserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('realiser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Realiser', function($stateParams, Realiser) {
                    return Realiser.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('realiser.new', {
            parent: 'realiser',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/realiser/realiser-dialog.html',
                    controller: 'RealiserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('realiser', null, { reload: true });
                }, function() {
                    $state.go('realiser');
                });
            }]
        })
        .state('realiser.edit', {
            parent: 'realiser',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/realiser/realiser-dialog.html',
                    controller: 'RealiserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Realiser', function(Realiser) {
                            return Realiser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('realiser', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('realiser.delete', {
            parent: 'realiser',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/realiser/realiser-delete-dialog.html',
                    controller: 'RealiserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Realiser', function(Realiser) {
                            return Realiser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('realiser', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
