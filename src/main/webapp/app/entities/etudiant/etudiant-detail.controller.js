(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('EtudiantDetailController', EtudiantDetailController);

    EtudiantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Etudiant', 'Utilisateur'];

    function EtudiantDetailController($scope, $rootScope, $stateParams, entity, Etudiant, Utilisateur) {
        var vm = this;

        vm.etudiant = entity;

        var unsubscribe = $rootScope.$on('projetApp:etudiantUpdate', function(event, result) {
            vm.etudiant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
