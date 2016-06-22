(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('ProfesseurDeleteController',ProfesseurDeleteController);

    ProfesseurDeleteController.$inject = ['$uibModalInstance', 'entity', 'Professeur'];

    function ProfesseurDeleteController($uibModalInstance, entity, Professeur) {
        var vm = this;

        vm.professeur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Professeur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
