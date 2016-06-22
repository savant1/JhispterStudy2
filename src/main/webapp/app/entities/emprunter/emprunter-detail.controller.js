(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('EmprunterDetailController', EmprunterDetailController);

    EmprunterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Emprunter', 'Ouvrage', 'Utilisateur'];

    function EmprunterDetailController($scope, $rootScope, $stateParams, entity, Emprunter, Ouvrage, Utilisateur) {
        var vm = this;

        vm.emprunter = entity;

        var unsubscribe = $rootScope.$on('projetApp:emprunterUpdate', function(event, result) {
            vm.emprunter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
