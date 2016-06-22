(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('OuvrageDetailController', OuvrageDetailController);

    OuvrageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Ouvrage', 'Realiser', 'Emprunter', 'Commenter', 'Noter', 'Recommander'];

    function OuvrageDetailController($scope, $rootScope, $stateParams, entity, Ouvrage, Realiser, Emprunter, Commenter, Noter, Recommander) {
        var vm = this;

        vm.ouvrage = entity;

        var unsubscribe = $rootScope.$on('projetApp:ouvrageUpdate', function(event, result) {
            vm.ouvrage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
