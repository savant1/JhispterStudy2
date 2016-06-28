/**
 * Created by ferry on 23/06/16.
 */

(function() {
    'use strict';

    angular
        .module('projetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('springdata', {
                parent: 'entity',
                url: '/springdata',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Spring Data'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/springdata/springdata.html',
                        controller: 'springdata',
                        controllerAs: 'vm'
                    }
                }
            });
    }

})();

