(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('RecommanderDialogController', RecommanderDialogController);

    RecommanderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Recommander', 'Ouvrage', 'Utilisateur'];

    function RecommanderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Recommander, Ouvrage, Utilisateur) {
        var vm = this;

        vm.recommander = entity;
        vm.clear = clear;
        vm.save = save;
        vm.ouvrages = Ouvrage.query({filter: 'recommander-is-null'});
        $q.all([vm.recommander.$promise, vm.ouvrages.$promise]).then(function() {
            if (!vm.recommander.ouvrage || !vm.recommander.ouvrage.id) {
                return $q.reject();
            }
            return Ouvrage.get({id : vm.recommander.ouvrage.id}).$promise;
        }).then(function(ouvrage) {
            vm.ouvrages.push(ouvrage);
        });
        vm.utilisateurs = Utilisateur.query({filter: 'recommander-is-null'});
        $q.all([vm.recommander.$promise, vm.utilisateurs.$promise]).then(function() {
            if (!vm.recommander.utilisateur || !vm.recommander.utilisateur.id) {
                return $q.reject();
            }
            return Utilisateur.get({id : vm.recommander.utilisateur.id}).$promise;
        }).then(function(utilisateur) {
            vm.utilisateurs.push(utilisateur);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.recommander.id !== null) {
                Recommander.update(vm.recommander, onSaveSuccess, onSaveError);
            } else {
                Recommander.save(vm.recommander, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projetApp:recommanderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
