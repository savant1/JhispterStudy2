(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('AuteurDeleteController',AuteurDeleteController);

    AuteurDeleteController.$inject = ['$uibModalInstance', 'entity', 'Auteur'];

    function AuteurDeleteController($uibModalInstance, entity, Auteur) {
        var vm = this;

        vm.auteur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Auteur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
