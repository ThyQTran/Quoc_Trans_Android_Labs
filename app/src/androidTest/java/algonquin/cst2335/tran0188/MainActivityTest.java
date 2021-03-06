package algonquin.cst2335.tran0188;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * JUnit test to confirm that password submitted is invalid
     */
    @Test
    public void mainActivityTest() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));

        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.button));

        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));

        textView.check(matches(withText("You shall not pass!")));

    }

    /**
     * JUnit test to confirm that password submitted without an uppercase character is invalid
     */
    @Test
    public void testFindMissingUpperCase(){

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));

        appCompatEditText.perform(replaceText("password123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.button));

        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));

        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * JUnit test to confirm that password submitted without a lowercase character is invalid
     */
    @Test
    public void testFindMissingLowerCase(){

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));

        appCompatEditText.perform(replaceText("PASSWORD123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.button));

        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));

        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * JUnit test to confirm that password submitted without a number character is invalid
     */
    @Test
    public void testFindMissingDigit(){

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));

        appCompatEditText.perform(replaceText("Password#$*"));

        ViewInteraction materialButton = onView(withId(R.id.button));

        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));

        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * JUnit test to confirm that password submitted without a special character (#$%^&*!@?) is invalid
     */
    @Test
    public void testFindMissingSpecialCharacter(){

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));

        appCompatEditText.perform(replaceText("Password123"));

        ViewInteraction materialButton = onView(withId(R.id.button));

        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));

        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * JUnit test to confirm that password submitted with an uppercase character, a lowercase
     * character, a number character and a special character (#$%^&*!@?) is valid
     */
    @Test
    public void testFindValidPassword(){

        ViewInteraction appCompatEditText = onView(withId(R.id.editText));

        appCompatEditText.perform(replaceText("Password123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.button));

        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));

        textView.check(matches(withText("Your password meets the requirements")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
