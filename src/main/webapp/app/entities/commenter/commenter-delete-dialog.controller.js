(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('CommenterDeleteController',CommenterDeleteController);

    CommenterDeleteController.$inject = ['$uibModalInstance', 'entity', 'Commenter'];

    function CommenterDeleteController($uibModalInstance, entity, Commenter) {
        var vm = this;

        vm.commenter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Commenter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
