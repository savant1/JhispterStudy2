(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('EmprunterDeleteController',EmprunterDeleteController);

    EmprunterDeleteController.$inject = ['$uibModalInstance', 'entity', 'Emprunter'];

    function EmprunterDeleteController($uibModalInstance, entity, Emprunter) {
        var vm = this;

        vm.emprunter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Emprunter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
