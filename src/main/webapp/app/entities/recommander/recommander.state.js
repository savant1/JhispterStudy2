(function() {
    'use strict';

    angular
        .module('projetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('recommander', {
            parent: 'entity',
            url: '/recommander?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.recommander.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/recommander/recommanders.html',
                    controller: 'RecommanderController',
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
                    $translatePartialLoader.addPart('recommander');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('recommander-detail', {
            parent: 'entity',
            url: '/recommander/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.recommander.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/recommander/recommander-detail.html',
                    controller: 'RecommanderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recommander');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Recommander', function($stateParams, Recommander) {
                    return Recommander.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('recommander.new', {
            parent: 'recommander',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recommander/recommander-dialog.html',
                    controller: 'RecommanderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                raison: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('recommander', null, { reload: true });
                }, function() {
                    $state.go('recommander');
                });
            }]
        })
        .state('recommander.edit', {
            parent: 'recommander',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recommander/recommander-dialog.html',
                    controller: 'RecommanderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Recommander', function(Recommander) {
                            return Recommander.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('recommander', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('recommander.delete', {
            parent: 'recommander',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recommander/recommander-delete-dialog.html',
                    controller: 'RecommanderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Recommander', function(Recommander) {
                            return Recommander.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('recommander', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
