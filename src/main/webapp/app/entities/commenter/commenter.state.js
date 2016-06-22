(function() {
    'use strict';

    angular
        .module('projetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('commenter', {
            parent: 'entity',
            url: '/commenter?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.commenter.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commenter/commenters.html',
                    controller: 'CommenterController',
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
                    $translatePartialLoader.addPart('commenter');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('commenter-detail', {
            parent: 'entity',
            url: '/commenter/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.commenter.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commenter/commenter-detail.html',
                    controller: 'CommenterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commenter');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Commenter', function($stateParams, Commenter) {
                    return Commenter.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('commenter.new', {
            parent: 'commenter',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commenter/commenter-dialog.html',
                    controller: 'CommenterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('commenter', null, { reload: true });
                }, function() {
                    $state.go('commenter');
                });
            }]
        })
        .state('commenter.edit', {
            parent: 'commenter',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commenter/commenter-dialog.html',
                    controller: 'CommenterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Commenter', function(Commenter) {
                            return Commenter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commenter', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commenter.delete', {
            parent: 'commenter',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commenter/commenter-delete-dialog.html',
                    controller: 'CommenterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Commenter', function(Commenter) {
                            return Commenter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commenter', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
