(function() {
    'use strict';

    angular
        .module('projetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('emprunter', {
            parent: 'entity',
            url: '/emprunter?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.emprunter.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/emprunter/emprunters.html',
                    controller: 'EmprunterController',
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
                    $translatePartialLoader.addPart('emprunter');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('emprunter-detail', {
            parent: 'entity',
            url: '/emprunter/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'projetApp.emprunter.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/emprunter/emprunter-detail.html',
                    controller: 'EmprunterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('emprunter');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Emprunter', function($stateParams, Emprunter) {
                    return Emprunter.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('emprunter.new', {
            parent: 'emprunter',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/emprunter/emprunter-dialog.html',
                    controller: 'EmprunterDialogController',
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
                    $state.go('emprunter', null, { reload: true });
                }, function() {
                    $state.go('emprunter');
                });
            }]
        })
        .state('emprunter.edit', {
            parent: 'emprunter',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/emprunter/emprunter-dialog.html',
                    controller: 'EmprunterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Emprunter', function(Emprunter) {
                            return Emprunter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('emprunter', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('emprunter.delete', {
            parent: 'emprunter',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/emprunter/emprunter-delete-dialog.html',
                    controller: 'EmprunterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Emprunter', function(Emprunter) {
                            return Emprunter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('emprunter', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
