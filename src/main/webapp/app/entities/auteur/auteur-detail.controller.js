(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('AuteurDetailController', AuteurDetailController);

    AuteurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Auteur', 'Realiser'];

    function AuteurDetailController($scope, $rootScope, $stateParams, entity, Auteur, Realiser) {
        var vm = this;

        vm.auteur = entity;

        var unsubscribe = $rootScope.$on('projetApp:auteurUpdate', function(event, result) {
            vm.auteur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
