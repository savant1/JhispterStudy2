'use strict';

describe('Controller Tests', function() {

    describe('Utilisateur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUtilisateur, MockEmprunter, MockCommenter, MockNoter, MockRecommander;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            MockEmprunter = jasmine.createSpy('MockEmprunter');
            MockCommenter = jasmine.createSpy('MockCommenter');
            MockNoter = jasmine.createSpy('MockNoter');
            MockRecommander = jasmine.createSpy('MockRecommander');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Utilisateur': MockUtilisateur,
                'Emprunter': MockEmprunter,
                'Commenter': MockCommenter,
                'Noter': MockNoter,
                'Recommander': MockRecommander
            };
            createController = function() {
                $injector.get('$controller')("UtilisateurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projetApp:utilisateurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
