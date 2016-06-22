'use strict';

describe('Controller Tests', function() {

    describe('Ouvrage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOuvrage, MockRealiser, MockEmprunter, MockCommenter, MockNoter, MockRecommander;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOuvrage = jasmine.createSpy('MockOuvrage');
            MockRealiser = jasmine.createSpy('MockRealiser');
            MockEmprunter = jasmine.createSpy('MockEmprunter');
            MockCommenter = jasmine.createSpy('MockCommenter');
            MockNoter = jasmine.createSpy('MockNoter');
            MockRecommander = jasmine.createSpy('MockRecommander');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Ouvrage': MockOuvrage,
                'Realiser': MockRealiser,
                'Emprunter': MockEmprunter,
                'Commenter': MockCommenter,
                'Noter': MockNoter,
                'Recommander': MockRecommander
            };
            createController = function() {
                $injector.get('$controller')("OuvrageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projetApp:ouvrageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
