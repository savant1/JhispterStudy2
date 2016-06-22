(function() {
    'use strict';

    angular
        .module('projetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('noter', {
            parent: 'entity',
            url: '/noter?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.noter.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/noter/noters.html',
                    controller: 'NoterController',
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
                    $translatePartialLoader.addPart('noter');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('noter-detail', {
            parent: 'entity',
            url: '/noter/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.noter.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/noter/noter-detail.html',
                    controller: 'NoterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('noter');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Noter', function($stateParams, Noter) {
                    return Noter.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('noter.new', {
            parent: 'noter',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/noter/noter-dialog.html',
                    controller: 'NoterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                note: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('noter', null, { reload: true });
                }, function() {
                    $state.go('noter');
                });
            }]
        })
        .state('noter.edit', {
            parent: 'noter',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/noter/noter-dialog.html',
                    controller: 'NoterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Noter', function(Noter) {
                            return Noter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('noter', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('noter.delete', {
            parent: 'noter',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/noter/noter-delete-dialog.html',
                    controller: 'NoterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Noter', function(Noter) {
                            return Noter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('noter', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
